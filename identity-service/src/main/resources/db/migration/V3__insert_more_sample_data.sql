-- Insert more users with different roles
INSERT INTO users (username, email, password, role)
VALUES
    -- More Administrators
    ('admin4', 'admin4@healthcare.com', '$2a$10$xn3LI/AjqicFYZFruSwve.ODd6/3W9LSdaq/KId3/F51/vHRuo6O2', 'ADMIN'),
    ('admin5', 'admin5@healthcare.com', '$2a$10$xn3LI/AjqicFYZFruSwve.ODd6/3W9LSdaq/KId3/F51/vHRuo6O2', 'ADMIN'),
    ('admin6', 'admin6@healthcare.com', '$2a$10$xn3LI/AjqicFYZFruSwve.ODd6/3W9LSdaq/KId3/F51/vHRuo6O2', 'ADMIN'),
    ('admin7', 'admin7@healthcare.com', '$2a$10$xn3LI/AjqicFYZFruSwve.ODd6/3W9LSdaq/KId3/F51/vHRuo6O2', 'ADMIN'),
    ('admin8', 'admin8@healthcare.com', '$2a$10$xn3LI/AjqicFYZFruSwve.ODd6/3W9LSdaq/KId3/F51/vHRuo6O2', 'ADMIN'),

    -- More Doctors (Specialists)
    ('drcardoso', 'drcardoso@healthcare.com', '$2a$10$xn3LI/AjqicFYZFruSwve.ODd6/3W9LSdaq/KId3/F51/vHRuo6O2', 'DOCTOR'),
    ('dralmeida', 'dralmeida@healthcare.com', '$2a$10$xn3LI/AjqicFYZFruSwve.ODd6/3W9LSdaq/KId3/F51/vHRuo6O2', 'DOCTOR'),
    ('drribeiro', 'drribeiro@healthcare.com', '$2a$10$xn3LI/AjqicFYZFruSwve.ODd6/3W9LSdaq/KId3/F51/vHRuo6O2', 'DOCTOR'),
    ('drlima', 'drlima@healthcare.com', '$2a$10$xn3LI/AjqicFYZFruSwve.ODd6/3W9LSdaq/KId3/F51/vHRuo6O2', 'DOCTOR'),
    ('drgomes', 'drgomes@healthcare.com', '$2a$10$xn3LI/AjqicFYZFruSwve.ODd6/3W9LSdaq/KId3/F51/vHRuo6O2', 'DOCTOR'),
    ('drcarvalho', 'drcarvalho@healthcare.com', '$2a$10$xn3LI/AjqicFYZFruSwve.ODd6/3W9LSdaq/KId3/F51/vHRuo6O2', 'DOCTOR'),
    ('drmartins', 'drmartins@healthcare.com', '$2a$10$xn3LI/AjqicFYZFruSwve.ODd6/3W9LSdaq/KId3/F51/vHRuo6O2', 'DOCTOR'),
    ('drrocha', 'drrocha@healthcare.com', '$2a$10$xn3LI/AjqicFYZFruSwve.ODd6/3W9LSdaq/KId3/F51/vHRuo6O2', 'DOCTOR'),

    -- More Patients
    ('patient6', 'patient6@email.com', '$2a$10$xn3LI/AjqicFYZFruSwve.ODd6/3W9LSdaq/KId3/F51/vHRuo6O2', 'PATIENT'),
    ('patient7', 'patient7@email.com', '$2a$10$xn3LI/AjqicFYZFruSwve.ODd6/3W9LSdaq/KId3/F51/vHRuo6O2', 'PATIENT'),
    ('patient8', 'patient8@email.com', '$2a$10$xn3LI/AjqicFYZFruSwve.ODd6/3W9LSdaq/KId3/F51/vHRuo6O2', 'PATIENT'),
    ('patient9', 'patient9@email.com', '$2a$10$xn3LI/AjqicFYZFruSwve.ODd6/3W9LSdaq/KId3/F51/vHRuo6O2', 'PATIENT'),
    ('patient10', 'patient10@email.com', '$2a$10$xn3LI/AjqicFYZFruSwve.ODd6/3W9LSdaq/KId3/F51/vHRuo6O2', 'PATIENT'),
    ('patient11', 'patient11@email.com', '$2a$10$xn3LI/AjqicFYZFruSwve.ODd6/3W9LSdaq/KId3/F51/vHRuo6O2', 'PATIENT'),
    ('patient12', 'patient12@email.com', '$2a$10$xn3LI/AjqicFYZFruSwve.ODd6/3W9LSdaq/KId3/F51/vHRuo6O2', 'PATIENT'),
    ('patient13', 'patient13@email.com', '$2a$10$xn3LI/AjqicFYZFruSwve.ODd6/3W9LSdaq/KId3/F51/vHRuo6O2', 'PATIENT'),
    ('patient14', 'patient14@email.com', '$2a$10$xn3LI/AjqicFYZFruSwve.ODd6/3W9LSdaq/KId3/F51/vHRuo6O2', 'PATIENT'),
    ('patient15', 'patient15@email.com', '$2a$10$xn3LI/AjqicFYZFruSwve.ODd6/3W9LSdaq/KId3/F51/vHRuo6O2', 'PATIENT');
