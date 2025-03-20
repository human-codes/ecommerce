//package uz.pdp.salemartpro.service;
//
//import com.pengrad.telegrambot.TelegramBot;
//import com.pengrad.telegrambot.UpdatesListener;
//import com.pengrad.telegrambot.model.Message;
//import com.pengrad.telegrambot.model.Update;
//import com.pengrad.telegrambot.request.SendMessage;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//import uz.pdp.salemartpro.entity.User;
//import uz.pdp.salemartpro.repo.UserRepository;
//
//
//import java.util.Optional;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class TelegramBotService {
//    private final TelegramBot telegramBot;
//    private final UserRepository userRepository;
//    private final RedisService redisService;
//
//    private final ConcurrentHashMap<Long, Boolean> awaitingUsername = new ConcurrentHashMap<>();
//
//    @PostConstruct
//    public void startBot() {
//        telegramBot.setUpdatesListener(updates -> {
//            for (Update update : updates) {
//                handleUpdate(update);
//            }
//            return UpdatesListener.CONFIRMED_UPDATES_ALL;
//        });
//        log.info("🚀 Telegram bot started listening for updates.");
//    }
//
//    @Async
//    public void handleUpdate(Update update) {
//        if (update.message() == null) return;
//
//        Message message = update.message();
//        Long telegramUserId = message.from().id();
//
//        // Step 1: Ask for username
//        if (message.text() != null && message.text().equals("/start")) {
//            telegramBot.execute(new SendMessage(telegramUserId, "Please send your website username."));
//            awaitingUsername.put(telegramUserId, true);
//            return;
//        }
//
//        // Step 2: Handle username input
//        if (message.text() != null && awaitingUsername.getOrDefault(telegramUserId, false)) {
//            String username = message.text();
//            Optional<User> user = userRepository.findByUsername(username);
//
//            if (user.isPresent()) {
//                user.get().setTelegramId(telegramUserId); // Assuming you have this field in your User entity
//                userRepository.save(user.get());
//                telegramBot.execute(new SendMessage(telegramUserId, "✅ Your Telegram account is linked! Now send your location 📍"));
//                awaitingUsername.remove(telegramUserId);
//            } else {
//                telegramBot.execute(new SendMessage(telegramUserId, "❌ Username not found. Please try again."));
//            }
//            return;
//        }
//
//        // Step 3: Handle location input
//        if (message.location() != null) {
//            User user = userRepository.findByTelegramId(telegramUserId);
//            if (user == null) {
//                telegramBot.execute(new SendMessage(telegramUserId, "❌ You need to link your account first. Send your username."));
//                awaitingUsername.put(telegramUserId, true);
//                return;
//            }
//
//            double lat = message.location().latitude();
//            double lon = message.location().longitude();
//            String location = lat + "," + lon;
//
//            redisService.saveLocation(user.getTelegramId(), location); // Save location using user ID
//            telegramBot.execute(new SendMessage(telegramUserId, "✅ Location saved! You can return to the basket."));
//            log.info("📍 Location saved for user {}: {}", user.getId(), location);
//        }
//    }
//}
