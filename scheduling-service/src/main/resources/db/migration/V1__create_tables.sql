CREATE TABLE appointments (
    id BIGSERIAL PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    healthcare_facility_id BIGINT,
    appointment_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    appointment_type VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    referral_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE referrals (
    id BIGSERIAL PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    requested_by_doctor_id BIGINT NOT NULL,
    referral_type VARCHAR(20) NOT NULL,
    priority_level VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    referral_reason TEXT NOT NULL,
    requested_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_appointments_patient ON appointments(patient_id);
CREATE INDEX idx_appointments_doctor ON appointments(doctor_id);
CREATE INDEX idx_appointments_facility ON appointments(healthcare_facility_id);
CREATE INDEX idx_appointments_date ON appointments(appointment_date);
CREATE INDEX idx_appointments_referral ON appointments(referral_id);
CREATE INDEX idx_referrals_patient ON referrals(patient_id);
CREATE INDEX idx_referrals_doctor ON referrals(requested_by_doctor_id);
