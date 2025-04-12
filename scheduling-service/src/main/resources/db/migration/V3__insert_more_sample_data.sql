-- Insert more appointments with varied scenarios
INSERT INTO appointments (patient_id, doctor_id, facility_id, appointment_date, start_time, end_time, status, notes)
VALUES
    -- Regular check-ups
    (6, 4, 6, '2025-04-20', '09:00', '09:30', 'SCHEDULED', 'Annual wellness check'),
    (7, 4, 6, '2025-04-20', '10:00', '10:30', 'SCHEDULED', 'Routine blood pressure monitoring'),
    (8, 4, 6, '2025-04-20', '11:00', '11:30', 'SCHEDULED', 'Diabetes follow-up'),
    
    -- Specialist consultations
    (9, 5, 7, '2025-04-21', '14:00', '14:45', 'SCHEDULED', 'Cardiology consultation'),
    (10, 5, 7, '2025-04-21', '15:00', '15:45', 'SCHEDULED', 'Post-surgery follow-up'),
    (11, 5, 7, '2025-04-21', '16:00', '16:45', 'RESCHEDULED', 'Rescheduled due to doctor emergency'),
    
    -- Emergency appointments
    (12, 6, 8, '2025-04-22', '08:00', '08:30', 'COMPLETED', 'Acute respiratory symptoms'),
    (13, 6, 8, '2025-04-22', '09:00', '09:30', 'COMPLETED', 'Severe allergic reaction'),
    
    -- Pediatric appointments
    (14, 7, 9, '2025-04-23', '10:00', '10:30', 'SCHEDULED', 'Child vaccination'),
    (15, 7, 9, '2025-04-23', '11:00', '11:30', 'SCHEDULED', 'Growth development check'),
    
    -- Telemedicine appointments
    (16, 8, 10, '2025-04-24', '13:00', '13:30', 'SCHEDULED', 'Virtual consultation - medication review'),
    (17, 8, 10, '2025-04-24', '14:00', '14:30', 'SCHEDULED', 'Virtual consultation - test results review'),
    
    -- Weekend appointments
    (18, 9, 11, '2025-04-26', '09:00', '09:30', 'SCHEDULED', 'Weekend clinic - urgent care'),
    (19, 9, 11, '2025-04-26', '10:00', '10:30', 'SCHEDULED', 'Weekend clinic - follow-up'),
    (20, 9, 11, '2025-04-26', '11:00', '11:30', 'WAITING_LIST', 'Weekend clinic - standby appointment');

-- Insert more referrals with different specialties and priorities
INSERT INTO referrals (patient_id, requested_by_doctor_id, target_specialty, target_facility_id, priority, status, reason, notes)
VALUES
    -- Cardiology referrals
    (6, 4, 'CARDIOLOGY', 6, 'HIGH', 'PENDING', 'Abnormal heart rhythm detected', 'ECG shows irregular patterns'),
    (7, 4, 'CARDIOLOGY', 6, 'URGENT', 'IN_PROGRESS', 'Chest pain investigation', 'Patient reports frequent chest pain'),
    
    -- Orthopedics referrals
    (8, 5, 'ORTHOPEDICS', 7, 'NORMAL', 'PENDING', 'Chronic back pain', 'MRI recommended for lower back'),
    (9, 5, 'ORTHOPEDICS', 7, 'HIGH', 'ACCEPTED', 'Sports injury - knee', 'Possible meniscus tear'),
    
    -- Neurology referrals
    (10, 6, 'NEUROLOGY', 8, 'URGENT', 'IN_PROGRESS', 'Severe headaches', 'Possible cluster headaches'),
    (11, 6, 'NEUROLOGY', 8, 'HIGH', 'PENDING', 'Numbness in extremities', 'Progressive symptoms over 3 months'),
    
    -- Endocrinology referrals
    (12, 7, 'ENDOCRINOLOGY', 9, 'NORMAL', 'ACCEPTED', 'Thyroid function review', 'Abnormal TSH levels'),
    (13, 7, 'ENDOCRINOLOGY', 9, 'LOW', 'PENDING', 'Diabetes management', 'HbA1c review needed'),
    
    -- Dermatology referrals
    (14, 8, 'DERMATOLOGY', 10, 'NORMAL', 'PENDING', 'Suspicious mole', 'Changed in size and color'),
    (15, 8, 'DERMATOLOGY', 10, 'LOW', 'ACCEPTED', 'Chronic eczema', 'Not responding to current treatment'),
    
    -- Psychiatry referrals
    (16, 9, 'PSYCHIATRY', 11, 'HIGH', 'IN_PROGRESS', 'Severe anxiety', 'Affecting daily activities'),
    (17, 9, 'PSYCHIATRY', 11, 'URGENT', 'PENDING', 'Depression screening', 'Recent major life changes'),
    
    -- Ophthalmology referrals
    (18, 10, 'OPHTHALMOLOGY', 12, 'NORMAL', 'ACCEPTED', 'Vision changes', 'Gradual deterioration in right eye'),
    (19, 10, 'OPHTHALMOLOGY', 12, 'HIGH', 'PENDING', 'Glaucoma suspect', 'High intraocular pressure'),
    
    -- Pulmonology referrals
    (20, 11, 'PULMONOLOGY', 13, 'URGENT', 'IN_PROGRESS', 'Chronic cough', 'Not responding to antibiotics');
