package dyn.controllers;

/**
 * Created by OM on 06.12.2017.
 */


import dyn.model.User;
import dyn.repository.UserRepository;
import dyn.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;

@Controller
public class PasswordController {
    private static final Logger logger = LoggerFactory.getLogger(PasswordController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // Display forgotPassword page
    @RequestMapping(value = "/forgot", method = RequestMethod.GET)
    public String displayForgotPasswordPage() {
        return "forgot";
    }

    // Process form submission from forgotPassword page
    @RequestMapping(value = "/forgot", method = RequestMethod.POST)
    public String processForgotPasswordForm(ModelAndView modelAndView, @RequestParam("email") String userEmail,
                                            HttpServletRequest request, RedirectAttributes redirectAttributes) {

        // Lookup user in database by e-mail
        User user = userRepository.findByEmail(userEmail);

        if (user == null) {
            redirectAttributes.addFlashAttribute("mess", "Нет такого email в базе данных.");
            return "redirect:/forgot";
        } else {

            // Generate random 36-character string token for reset password
            String resetToken = UUID.randomUUID().toString();
            user.setResetToken(resetToken);

            logger.info("Reset password token will be saved for user " + user.getUserName() + " and sent to " + userEmail);

            // Save token to database
            userRepository.save(user);

            //String appUrl = request.getScheme() + "://" + request.getServerName();
            String appUrl = "localhost:8080"; // TODO: сделать автоматом для сервера

            // Email message
            SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
            passwordResetEmail.setFrom("dyngame@yandex.ru");
            passwordResetEmail.setTo(user.getEmail());
            passwordResetEmail.setSubject("Dyngame.ru: password reset request");
            passwordResetEmail.setText("Чтобы сбросить пароль на сайте dyngame.ru перейдите по ссылке:\n" + appUrl + "/reset?token=" + user.getResetToken());

            emailService.sendEmail(passwordResetEmail);
        }

        redirectAttributes.addFlashAttribute("mess", "Вам отправлено письмо со ссылкой для сброса пароля.");
        return "redirect:/login";

    }

    // Display form to reset password
    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public String displayResetPasswordPage(ModelMap model, @RequestParam("token") String token, RedirectAttributes redirectAttributes) {

        User user = userRepository.findByResetToken(token);

        if (user != null) { // Token found in DB
            model.addAttribute("resetToken", token);
        } else { // Token not found in DB
            redirectAttributes.addFlashAttribute("mess", "Ссылка для сброса пароля не верна.");
            return "redirect:/login";
        }

        return "resetPassword";
    }

    // Process reset password form
    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public String setNewPassword(ModelMap model, @RequestParam Map<String, String> requestParams,
                                 RedirectAttributes redir) {

        // Find the user associated with the reset token
        String token = requestParams.get("token");
        User user = userRepository.findByResetToken(token);

        String error = "";
        // This should always be non-null but we check just in case
        if (user != null) {
            // Set new password
            String password = requestParams.get("password");
            String passwordConfirm = requestParams.get("passwordConfirm");
            if (password.length() < 6) {
                error = error + "Пароль должен состоять хотя бы из 6 символов. ";
            }
            if (!password.equals(passwordConfirm)) {
                error = error + "Введенные пароли не совпадают. ";
            }

            if (!error.isEmpty()) {
                model.addAttribute("error", error);
                model.addAttribute("resetToken", token);
                return "resetPassword";
            }

            user.setPassword(bCryptPasswordEncoder.encode(password));

            // Set the reset token to null so it cannot be used again
            user.setResetToken(null);

            logger.info("Password will be updated for user " + user.getUserName());

            // Save user
            userRepository.save(user);

            // In order to set a model attribute on a redirect, we must use
            // RedirectAttributes
            redir.addFlashAttribute("mess", "Вы успешно изменили свой пароль.");
            return "redirect:/login";

        } else {
            redir.addFlashAttribute("mess", "Ссылка для сброса пароля не верна.");
            return "redirect:/login";
        }
    }

    // Going to reset page without a token redirects to login page
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ModelAndView handleMissingParams(MissingServletRequestParameterException ex) {
        return new ModelAndView("redirect:login");
    }
}