package com.cafe_be.config;

import com.cafe_be.model.*;
import com.cafe_be.model.enums.Role;
import com.cafe_be.repository.CafeTableRepository;
import com.cafe_be.repository.ProductRepository;
import com.cafe_be.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner init(UserRepository userRepo, ProductRepository productRepo, CafeTableRepository tableRepo, PasswordEncoder encoder) {
        return args -> {
            // Create root user if not exists
            if (userRepo.findByUsername("root").isEmpty()) {
                User root = new User();
                root.setUsername("root");
                root.setPassword(encoder.encode("rootpass"));
                root.setRole(Role.ROOT);
                root.setFullName("Root User");
                root.setEmail("root@example.com");
                userRepo.save(root);
            }

            // Add sample products if none exist
            if (productRepo.count() == 0) {
                // Coffee category
                Product mocha = new Product();
                mocha.setName("Mocha Recipe");
                mocha.setDescription("Mocha là 1 loại café được tạo từ Espresso & chocolate ngọt ngào. Hương vị chocolate hoà quyện với café tạo nên ly Mocha Recipe độc đáo chỉ có tại The Coffee House!");
                mocha.setPrice(49000.0);
                mocha.setCategory("COFFEE");
                mocha.setImage("https://product.hstatic.net/1000075078/product/mocha_2_copy_06822eb79b504e7e93fb7e27099d4325_large.jpg");
                mocha.setAvailable(true);
                productRepo.save(mocha);

                Product vanilla = new Product();
                vanilla.setName("Vanilla Latte");
                vanilla.setDescription("Ly cà phê sữa đậm đà thời thượng! Với vị vanilla ngọt ngào và thơm nồng, kết hợp hoàn hảo với cà phê espresso đậm đắng.");
                vanilla.setPrice(52000.0);
                vanilla.setCategory("COFFEE");
                vanilla.setImage("https://product.hstatic.net/1000075078/product/vanilla_latte_copy_a89546020c9f4105be3b57a84d780904_large.jpg");
                vanilla.setAvailable(true);
                productRepo.save(vanilla);

                Product latte = new Product();
                latte.setName("Latte");
                latte.setDescription("Ly cà phê sữa ngọt ngào đến khó quên! Với lớp kem sữa béo ngậy, quyện cùng cà phê espresso đậm đà.");
                latte.setPrice(50000.0);
                latte.setCategory("COFFEE");
                latte.setImage("https://product.hstatic.net/1000075078/product/latte_copy_063797_3d07decf0b1e478aacf5e0ce305a7d9d_large.jpg");
                latte.setAvailable(true);
                productRepo.save(latte);

                Product americano = new Product();
                americano.setName("Americano");
                americano.setDescription("Là sự kết hợp giữa espresso và nước sôi. Americano mang đến hương vị cà phê truyền thống đậm đà nhưng thoảng nhẹ và tinh tế.");
                americano.setPrice(46000.0);
                americano.setCategory("COFFEE");
                americano.setImage("https://product.hstatic.net/1000075078/product/americano_copy_e410cd658c9c44d3ab3dbf0df1806721_large.jpg");
                americano.setAvailable(true);
                productRepo.save(americano);

                Product espresso = new Product();
                espresso.setName("ICED Espresso");
                espresso.setDescription("Ly cà phê espresso ngon đậm đà! Được chiết xuất từ những hạt cà phê rang xay thơm nức.");
                espresso.setPrice(45000.0);
                espresso.setCategory("COFFEE");
                espresso.setImage("https://product.hstatic.net/1000075078/product/iced-espresso_0a1b9e7278614102b87b22bc44eeb37d_large.jpg");
                espresso.setAvailable(true);
                productRepo.save(espresso);

                Product caramelLatte = new Product();
                caramelLatte.setName("Caramel Latte");
                caramelLatte.setDescription("Ly cà phê sữa ngọt ngào đến khó quên! Với lớp caramel béo ngậy, quyện cùng cà phê espresso đậm đà.");
                caramelLatte.setPrice(54000.0);
                caramelLatte.setCategory("COFFEE");
                caramelLatte.setImage("https://product.hstatic.net/1000075078/product/caramel-latte_2_copy_7732faee390e4eea826f2fd9f5a75e7b_large.jpg");
                caramelLatte.setAvailable(true);
                productRepo.save(caramelLatte);

                // Tea category
                Product sencha = new Product();
                sencha.setName("Trà Xanh Sencha");
                sencha.setDescription("Trà xanh Sencha thanh mát với hương vị tinh tế. Được chọn lọc từ những lá trà non nhất.");
                sencha.setPrice(45000.0);
                sencha.setCategory("TEA");
                sencha.setImage("https://product.hstatic.net/1000075078/product/tra-xanh_14487db99f3f4dbda81d99963160f16d_large.jpg");
                sencha.setAvailable(true);
                productRepo.save(sencha);

                // Cake category
                Product tiramisu = new Product();
                tiramisu.setName("Bánh Tiramisu");
                tiramisu.setDescription("Bánh Tiramisu thơm béo với lớp kem mascarpone mềm mịn và hương cà phê đậm đà.");
                tiramisu.setPrice(35000.0);
                tiramisu.setCategory("CAKE");
                tiramisu.setImage("https://product.hstatic.net/1000075078/product/tiramisu_copy_eb7de160481c4a79aa90c6f8c4617d5f_large.jpg");
                tiramisu.setAvailable(true);
                productRepo.save(tiramisu);
            }

            // Add sample tables if none exist
            if (tableRepo.count() == 0) {
                for (int i = 1; i <= 10; i++) {
                    CafeTable table = new CafeTable();
                    table.setName("Bàn " + i);
                    table.setCapacity(4);
                    table.setAvailable(true);
                    tableRepo.save(table);
                }
            }
        };
    }
}
