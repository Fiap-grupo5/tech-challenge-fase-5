-- Insert sample administrators
INSERT INTO administrators (user_id, first_name, last_name, registration_number, department, phone_number)
VALUES
    (1, 'Carlos', 'Silva', 'ADM001', 'Hospital Administration', '11999990001'),
    (2, 'Maria', 'Santos', 'ADM002', 'Clinical Operations', '11999990002'),
    (3, 'João', 'Oliveira', 'ADM003', 'Facility Management', '11999990003');

-- Insert sample doctors
INSERT INTO doctors (user_id, first_name, last_name, crm, specialty, sub_specialty, phone_number)
VALUES
    (4, 'Ana', 'Silva', 'CRM123456', 'Cardiology', 'Interventional Cardiology', '11999990004'),
    (5, 'Pedro', 'Santos', 'CRM234567', 'Neurology', 'Neurosurgery', '11999990005'),
    (6, 'Mariana', 'Oliveira', 'CRM345678', 'Orthopedics', 'Sports Medicine', '11999990006'),
    (7, 'Lucas', 'Pereira', 'CRM456789', 'Pediatrics', 'Neonatology', '11999990007'),
    (8, 'Julia', 'Ferreira', 'CRM567890', 'Dermatology', 'Cosmetic Dermatology', '11999990008');

-- Insert sample patients
INSERT INTO patients (user_id, first_name, last_name, cpf, birth_date, gender, phone_number, emergency_contact, blood_type, allergies)
VALUES
    (9, 'Roberto', 'Almeida', '12345678901', '1980-05-15', 'MALE', '11999990009', '11988880001', 'A+', 'Penicillin'),
    (10, 'Fernanda', 'Costa', '23456789012', '1992-08-22', 'FEMALE', '11999990010', '11988880002', 'O-', 'Latex'),
    (11, 'Gabriel', 'Martins', '34567890123', '1975-03-10', 'MALE', '11999990011', '11988880003', 'B+', 'None'),
    (12, 'Carolina', 'Lima', '45678901234', '1988-11-30', 'FEMALE', '11999990012', '11988880004', 'AB+', 'Sulfa'),
    (13, 'Ricardo', 'Sousa', '56789012345', '1995-07-25', 'MALE', '11999990013', '11988880005', 'A-', 'Aspirin');

-- Insert sample medical records
INSERT INTO medical_records (patient_id, doctor_id, visit_date, symptoms, diagnosis, prescription, notes)
VALUES
    (1, 1, '2025-04-01', 'Chest pain, shortness of breath', 'Angina', 'Nitroglycerin 0.4mg', 'Follow-up in 2 weeks'),
    (2, 2, '2025-04-02', 'Severe headache, sensitivity to light', 'Migraine', 'Sumatriptan 50mg', 'MRI scheduled'),
    (3, 3, '2025-04-03', 'Knee pain after sports', 'Meniscus tear', 'Rest, ice, compression', 'Physical therapy recommended'),
    (4, 4, '2025-04-04', 'Fever, cough', 'Upper respiratory infection', 'Amoxicillin 500mg', 'Return if symptoms worsen'),
    (5, 5, '2025-04-05', 'Skin rash, itching', 'Contact dermatitis', 'Hydrocortisone cream', 'Allergy testing scheduled');
