-- Insert sample users with BCrypt-encoded passwords (password is 'password123' for all users)
INSERT INTO users (username, email, password, role)
VALUES
    -- Administrators
    ('admin1', 'admin1@healthcare.com', '$2a$10$xn3LI/AjqicFYZFruSwve.ODd6/3W9LSdaq/KId3/F51/vHRuo6O2', 'ADMIN'),
    ('admin2', 'admin2@healthcare.com', '$2a$10$xn3LI/AjqicFYZFruSwve.ODd6/3W9LSdaq/KId3/F51/vHRuo6O2', 'ADMIN'),
    ('admin3', 'admin3@healthcare.com', '$2a$10$xn3LI/AjqicFYZFruSwve.ODd6/3W9LSdaq/KId3/F51/vHRuo6O2', 'ADMIN'),
    
    -- Doctors
    ('drsilva', 'drsilva@healthcare.com', '$2a$10$xn3LI/AjqicFYZFruSwve.ODd6/3W9LSdaq/KId3/F51/vHRuo6O2', 'DOCTOR'),
    ('drsantos', 'drsantos@healthcare.com', '$2a$10$xn3LI/AjqicFYZFruSwve.ODd6/3W9LSdaq/KId3/F51/vHRuo6O2', 'DOCTOR'),
    ('droliveira', 'droliveira@healthcare.com', '$2a$10$xn3LI/AjqicFYZFruSwve.ODd6/3W9LSdaq/KId3/F51/vHRuo6O2', 'DOCTOR'),
    ('drpereira', 'drpereira@healthcare.com', '$2a$10$xn3LI/AjqicFYZFruSwve.ODd6/3W9LSdaq/KId3/F51/vHRuo6O2', 'DOCTOR'),
    ('drferreira', 'drferreira@healthcare.com', '$2a$10$xn3LI/AjqicFYZFruSwve.ODd6/3W9LSdaq/KId3/F51/vHRuo6O2', 'DOCTOR'),
    
    -- Patients
    ('patient1', 'patient1@email.com', '$2a$10$xn3LI/AjqicFYZFruSwve.ODd6/3W9LSdaq/KId3/F51/vHRuo6O2', 'PATIENT'),
    ('patient2', 'patient2@email.com', '$2a$10$xn3LI/AjqicFYZFruSwve.ODd6/3W9LSdaq/KId3/F51/vHRuo6O2', 'PATIENT'),
    ('patient3', 'patient3@email.com', '$2a$10$xn3LI/AjqicFYZFruSwve.ODd6/3W9LSdaq/KId3/F51/vHRuo6O2', 'PATIENT'),
    ('patient4', 'patient4@email.com', '$2a$10$xn3LI/AjqicFYZFruSwve.ODd6/3W9LSdaq/KId3/F51/vHRuo6O2', 'PATIENT'),
    ('patient5', 'patient5@email.com', '$2a$10$xn3LI/AjqicFYZFruSwve.ODd6/3W9LSdaq/KId3/F51/vHRuo6O2', 'PATIENT');
