-- Insert more administrators
INSERT INTO administrators (user_id, first_name, last_name, registration_number, department, phone_number)
VALUES
    (14, 'Patricia', 'Ribeiro', 'ADM004', 'Quality Assurance', '11999990014'),
    (15, 'Fernando', 'Gomes', 'ADM005', 'Patient Relations', '11999990015'),
    (16, 'Amanda', 'Carvalho', 'ADM006', 'Emergency Services', '11999990016'),
    (17, 'Bruno', 'Martins', 'ADM007', 'Surgical Operations', '11999990017'),
    (18, 'Camila', 'Rocha', 'ADM008', 'Outpatient Services', '11999990018');

-- Insert more doctors with various specialties
INSERT INTO doctors (user_id, first_name, last_name, crm, specialty, sub_specialty, phone_number)
VALUES
    (19, 'Rafael', 'Cardoso', 'CRM678901', 'Endocrinology', 'Diabetes Management', '11999990019'),
    (20, 'Beatriz', 'Almeida', 'CRM789012', 'Psychiatry', 'Child Psychiatry', '11999990020'),
    (21, 'Thiago', 'Ribeiro', 'CRM890123', 'Ophthalmology', 'Retina Specialist', '11999990021'),
    (22, 'Laura', 'Lima', 'CRM901234', 'Pulmonology', 'Sleep Medicine', '11999990022'),
    (23, 'Diego', 'Gomes', 'CRM012345', 'Gastroenterology', 'Hepatology', '11999990023'),
    (24, 'Isabela', 'Carvalho', 'CRM123450', 'Rheumatology', null, '11999990024'),
    (25, 'Vitor', 'Martins', 'CRM234501', 'Urology', 'Oncological Urology', '11999990025'),
    (26, 'Sofia', 'Rocha', 'CRM345012', 'Gynecology', 'Reproductive Medicine', '11999990026');

-- Insert more patients with varied medical conditions
INSERT INTO patients (user_id, first_name, last_name, cpf, birth_date, gender, phone_number, emergency_contact, blood_type, allergies)
VALUES
    (27, 'Miguel', 'Teixeira', '67890123456', '1982-06-18', 'MALE', '11999990027', '11988880006', 'O+', 'Iodine'),
    (28, 'Helena', 'Barbosa', '78901234567', '1990-09-25', 'FEMALE', '11999990028', '11988880007', 'B-', 'None'),
    (29, 'Arthur', 'Correia', '89012345678', '1978-12-03', 'MALE', '11999990029', '11988880008', 'AB-', 'Peanuts'),
    (30, 'Alice', 'Fernandes', '90123456789', '1995-02-14', 'FEMALE', '11999990030', '11988880009', 'O+', 'None'),
    (31, 'Davi', 'Rodrigues', '01234567890', '1987-07-22', 'MALE', '11999990031', '11988880010', 'A+', 'Shellfish'),
    (32, 'Valentina', 'Azevedo', '12345098765', '1993-04-11', 'FEMALE', '11999990032', '11988880011', 'B+', 'None'),
    (33, 'Lorenzo', 'Cavalcanti', '23450987654', '1980-11-29', 'MALE', '11999990033', '11988880012', 'O-', 'Dairy'),
    (34, 'Heloísa', 'Pinto', '34509876543', '1989-08-07', 'FEMALE', '11999990034', '11988880013', 'AB+', 'None'),
    (35, 'Théo', 'Moreira', '45098765432', '1997-01-15', 'MALE', '11999990035', '11988880014', 'A-', 'None'),
    (36, 'Stella', 'Cardoso', '50987654321', '1985-10-20', 'FEMALE', '11999990036', '11988880015', 'O+', 'Penicillin');

-- Insert more medical records with diverse conditions
INSERT INTO medical_records (patient_id, doctor_id, visit_date, symptoms, diagnosis, prescription, notes)
VALUES
    (6, 6, '2025-04-06', 'High blood sugar, frequent urination', 'Type 2 Diabetes', 'Metformin 500mg', 'Diet and exercise plan provided'),
    (7, 7, '2025-04-07', 'Anxiety, insomnia', 'Generalized Anxiety Disorder', 'Sertraline 50mg', 'Weekly therapy sessions recommended'),
    (8, 8, '2025-04-08', 'Blurred vision, eye strain', 'Diabetic Retinopathy', 'Eye drops', 'Monthly monitoring required'),
    (9, 9, '2025-04-09', 'Chronic cough, wheezing', 'Asthma', 'Albuterol inhaler', 'Pulmonary function test scheduled'),
    (10, 10, '2025-04-10', 'Abdominal pain, acid reflux', 'GERD', 'Omeprazole 20mg', 'Dietary modifications discussed'),
    (6, 11, '2025-04-11', 'Joint pain, morning stiffness', 'Rheumatoid Arthritis', 'Prednisone 5mg', 'Rheumatology follow-up in 1 month'),
    (7, 12, '2025-04-12', 'Frequent urination, back pain', 'Kidney Stones', 'Pain medication', 'CT scan scheduled'),
    (8, 13, '2025-04-13', 'Irregular menstruation', 'PCOS', 'Birth control pills', 'Hormone level monitoring'),
    (9, 6, '2025-04-14', 'Weight gain, fatigue', 'Hypothyroidism', 'Levothyroxine 50mcg', 'TSH monitoring every 6 weeks'),
    (10, 7, '2025-04-15', 'Depression, loss of appetite', 'Major Depressive Disorder', 'Fluoxetine 20mg', 'Biweekly counseling recommended');
