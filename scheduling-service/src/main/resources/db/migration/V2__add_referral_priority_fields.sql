ALTER TABLE referrals 
ADD COLUMN patient_age INTEGER,
ADD COLUMN is_pregnant BOOLEAN,
ADD COLUMN has_medical_urgency BOOLEAN;

COMMENT ON COLUMN referrals.patient_age IS 'Idade do paciente para cálculo de prioridade';
COMMENT ON COLUMN referrals.is_pregnant IS 'Indica se a paciente está grávida para priorização';
COMMENT ON COLUMN referrals.has_medical_urgency IS 'Indica condição médica que requer priorização'; 