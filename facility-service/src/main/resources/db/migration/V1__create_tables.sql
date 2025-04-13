CREATE TABLE healthcare_facilities (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    facility_type VARCHAR(20) NOT NULL,
    cnpj VARCHAR(14) NOT NULL UNIQUE,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(2) NOT NULL,
    zip_code VARCHAR(8) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    max_daily_capacity INTEGER NOT NULL,
    current_load INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE doctor_schedules (
    id BIGSERIAL PRIMARY KEY,
    doctor_id BIGINT NOT NULL,
    facility_id BIGINT NOT NULL,
    day_of_week VARCHAR(10) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    second_period_start TIME,
    second_period_end TIME,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (facility_id) REFERENCES healthcare_facilities(id),
    UNIQUE (doctor_id, facility_id, day_of_week)
);

CREATE TABLE administrator_facilities (
    id BIGSERIAL PRIMARY KEY,
    administrator_id BIGINT NOT NULL,
    healthcare_facility_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (healthcare_facility_id) REFERENCES healthcare_facilities(id),
    UNIQUE (administrator_id, healthcare_facility_id)
);

CREATE INDEX idx_facilities_cnpj ON healthcare_facilities(cnpj);
CREATE INDEX idx_facilities_city ON healthcare_facilities(city);
CREATE INDEX idx_doctor_schedules_doctor ON doctor_schedules(doctor_id);
CREATE INDEX idx_doctor_schedules_facility ON doctor_schedules(facility_id);
CREATE INDEX idx_administrator_facilities_admin ON administrator_facilities(administrator_id);
CREATE INDEX idx_administrator_facilities_facility ON administrator_facilities(healthcare_facility_id);
