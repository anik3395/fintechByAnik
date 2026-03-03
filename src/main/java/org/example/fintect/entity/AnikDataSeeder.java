package org.example.fintect.entity;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class AnikDataSeeder {

    private final AnikRepository anikRepository;

    public AnikDataSeeder(AnikRepository anikRepository) {
        this.anikRepository = anikRepository;
    }

    @PostConstruct
    public void seedData() {

        // Prevent duplicate insert on every restart
        if (anikRepository.count() > 0) {
            return;
        }

        Anik a1 = new Anik();
        a1.setName("Wireless Mouse");
        a1.setDescription("High precision wireless mouse");
        a1.setCategory("Electronics");
        a1.setPrice(750.0);

        Anik a2 = new Anik();
        a2.setName("Mechanical Keyboard");
        a2.setDescription("RGB mechanical keyboard");
        a2.setCategory("Electronics");
        a2.setPrice(4500.0);

        Anik a3 = new Anik();
        a3.setName("Office Chair");
        a3.setDescription("Ergonomic office chair");
        a3.setCategory("Furniture");
        a3.setPrice(8500.0);



        anikRepository.save(a1);
        anikRepository.save(a2);
        anikRepository.save(a3);
//        anikRepository.save(a4);
        System.out.println("✅ Anik seed data inserted using @PostConstruct");
    }
}

