//package uz.pdp.salemartpro.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.server.ResponseStatusException;
//import uz.pdp.salemartpro.entity.User;
//import uz.pdp.salemartpro.service.RedisService;
//
//@RestController
//@RequestMapping("/location")
//public class LocationController {
//
//    private final RedisService redisService;
//
//    @Autowired
//    public LocationController(RedisService redisService) {
//        this.redisService = redisService;
//    }
//
//    @GetMapping("/check")
//    public ResponseEntity<Boolean> checkLocation(@AuthenticationPrincipal User user) {
//        if (user == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//
//        Long telegramId = user.getTelegramId();
//        String location = redisService.getLocation(telegramId);
//
//        return ResponseEntity.ok(location != null); // Returns true if location exists, false otherwise
//    }
//}
