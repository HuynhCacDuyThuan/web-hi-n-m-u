//package com.example.demo.test;
//
//import com.example.demo.model.MedicalCondition;
//import com.example.demo.repository.MedicalConditionRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class DataLoader {
//
//	@Bean
//	CommandLineRunner initDatabase(MedicalConditionRepository repository) {
//		return args -> {
//			repository.save(new MedicalCondition("Không có bệnh."));
//			repository.save(new MedicalCondition("bệnh tim mạch."));
//			repository.save(new MedicalCondition("Huyết áp cao ."));
//			repository.save(new MedicalCondition("Huyết áp thấp."));
//			repository.save(new MedicalCondition("Có tiền sử phẫu thuật tim mạch, cắt hoặc ghép bộ phận."));
//			repository.save(new MedicalCondition("Đang bị thiếu máu."));
//			repository.save(new MedicalCondition("Có bệnh thấp khớp."));
//			repository.save(new MedicalCondition("Mắc bệnh đái tháo đường."));
//		};
//	}
//}
