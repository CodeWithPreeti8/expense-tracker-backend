package com.expensetracker.expense_tracker;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import com.expensetracker.expense_tracker.entity.Category;
import com.expensetracker.expense_tracker.repository.CategoryRepository;

@SpringBootApplication
@EnableAsync
public class ExpenseTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpenseTrackerApplication.class, args);
	}
	
	// Initialize fixed categories
    @Bean
    CommandLineRunner initCategories(CategoryRepository categoryRepository) {
        return args -> {
            if (categoryRepository.count() == 0) { // only if empty
                categoryRepository.save(new Category("Groceries"));
                categoryRepository.save(new Category("Rent"));
                categoryRepository.save(new Category("Entertainment"));
                categoryRepository.save(new Category("Transport"));
                categoryRepository.save(new Category("Utilities"));
                System.out.println("✅ Fixed categories initialized");
            }
        };
    }

}
