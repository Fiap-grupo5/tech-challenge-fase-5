-- Insert sample appointments
INSERT INTO appointments (patient_id, doctor_id, facility_id, appointment_date, start_time, end_time, status, notes)
VALUES
    (1, 1, 1, '2025-04-15', '09:00', '09:30', 'SCHEDULED', 'Regular check-up'),
    (2, 1, 1, '2025-04-15', '10:00', '10:30', 'SCHEDULED', 'Follow-up appointment'),
    (3, 2, 2, '2025-04-16', '14:00', '14:30', 'SCHEDULED', 'Initial consultation'),
    (4, 2, 2, '2025-04-16', '15:00', '15:30', 'CANCELLED', 'Patient requested cancellation'),
    (5, 3, 3, '2025-04-17', '11:00', '11:30', 'COMPLETED', 'Annual physical examination');

-- Insert sample referrals
INSERT INTO referrals (patient_id, requested_by_doctor_id, target_specialty, target_facility_id, priority, status, reason, notes)
VALUES
    (1, 1, 'CARDIOLOGY', 2, 'NORMAL', 'PENDING', 'Abnormal ECG results', 'Please evaluate for potential arrhythmia'),
    (2, 1, 'NEUROLOGY', 3, 'URGENT', 'IN_PROGRESS', 'Recurring migraines', 'Patient reports increasing frequency and severity'),
    (3, 2, 'ORTHOPEDICS', 1, 'NORMAL', 'COMPLETED', 'Chronic knee pain', 'MRI recommended'),
    (4, 2, 'DERMATOLOGY', 4, 'LOW', 'CANCELLED', 'Skin rash', 'Patient decided to consult their regular dermatologist'),
    (5, 3, 'ENDOCRINOLOGY', 5, 'HIGH', 'ACCEPTED', 'Thyroid abnormalities', 'Recent blood tests show elevated TSH levels');
