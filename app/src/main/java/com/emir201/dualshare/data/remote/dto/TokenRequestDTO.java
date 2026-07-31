package com.emir201.dualshare.data.remote.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public record TokenRequestDTO(
        String firebaseToken) {
}
