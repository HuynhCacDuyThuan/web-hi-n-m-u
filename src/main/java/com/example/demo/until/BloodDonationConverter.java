package com.example.demo.until;



import com.example.demo.dto.BloodDonationDTO;
import com.example.demo.model.Contract.BloodDonation;

public class BloodDonationConverter {

    // Method to convert BloodDonation (blockchain model) to BloodDonationDTO
    public static BloodDonationDTO toDTO(BloodDonation bloodDonation) {
        return new BloodDonationDTO(
            bloodDonation.getId(),                    // ID
            bloodDonation.getName(),                  // Name
            bloodDonation.getDonationDate(),          // Donation Date
            bloodDonation.getRegisteredDate(),        // Registered Date
            bloodDonation.getVolume(),                // Volume
            bloodDonation.getBloodType(),             // Blood Type
            bloodDonation.getStatus(),                // Status
            bloodDonation.getUserId(),                // User ID
            bloodDonation.getCertificationIssued()    // Certification Issued
        );
    }
}
