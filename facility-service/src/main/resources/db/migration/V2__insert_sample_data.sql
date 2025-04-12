-- Insert sample healthcare facilities in São Paulo
INSERT INTO healthcare_facilities (name, address, city, state, zip_code, phone_number, latitude, longitude, max_daily_capacity, current_load)
VALUES
    ('Hospital Israelita Albert Einstein', 'Av. Albert Einstein, 627', 'São Paulo', 'SP', '05652900', '1121511233', -23.6000, -46.7150, 500, 250),
    ('Hospital Sírio-Libanês', 'Rua Dona Adma Jafet, 91', 'São Paulo', 'SP', '01308050', '1133940200', -23.5580, -46.6520, 400, 180),
    ('Hospital Alemão Oswaldo Cruz', 'Rua 13 de Maio, 1815', 'São Paulo', 'SP', '01327001', '1135495000', -23.5690, -46.6440, 300, 120),
    ('Hospital São Paulo', 'Rua Napoleão de Barros, 715', 'São Paulo', 'SP', '04024002', '1155764848', -23.5970, -46.6430, 450, 200),
    ('Hospital Santa Catarina', 'Av. Paulista, 200', 'São Paulo', 'SP', '01310000', '1132855000', -23.5700, -46.6460, 350, 150);

-- Insert sample doctor schedules
INSERT INTO doctor_schedules (doctor_id, facility_id, day_of_week, start_time, end_time)
VALUES
    (1, 1, 1, '08:00', '18:00'),
    (1, 2, 2, '08:00', '18:00'),
    (2, 1, 3, '09:00', '19:00'),
    (2, 3, 4, '09:00', '19:00'),
    (3, 2, 5, '08:00', '16:00');

-- Insert sample administrator assignments
INSERT INTO administrator_facilities (administrator_id, facility_id)
VALUES
    (1, 1),
    (1, 2),
    (2, 3),
    (2, 4),
    (3, 5);
