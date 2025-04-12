-- Insert more healthcare facilities in other cities
INSERT INTO healthcare_facilities (name, address, city, state, zip_code, phone_number, latitude, longitude, max_daily_capacity, current_load)
VALUES
    -- Rio de Janeiro
    ('Hospital Copa D''Or', 'Rua Figueiredo Magalhães, 875', 'Rio de Janeiro', 'RJ', '22031070', '2121275000', -22.9697, -43.1869, 400, 180),
    ('Hospital Samaritano', 'Rua Bambina, 98', 'Rio de Janeiro', 'RJ', '22251050', '2121277000', -22.9520, -43.1837, 350, 160),
    ('Hospital São Lucas', 'Rua Engenheiro Cortes Sigaud, 155', 'Rio de Janeiro', 'RJ', '22450150', '2123828200', -22.9723, -43.2173, 300, 140),

    -- Curitiba
    ('Hospital Nossa Senhora das Graças', 'Rua Alcides Munhoz, 433', 'Curitiba', 'PR', '80810040', '4133103000', -25.4284, -49.2733, 300, 150),
    ('Hospital Marcelino Champagnat', 'Av. Presidente Affonso Camargo, 1399', 'Curitiba', 'PR', '80050370', '4132161600', -25.4352, -49.2541, 250, 120),

    -- Belo Horizonte
    ('Hospital Mater Dei', 'Av. do Contorno, 9000', 'Belo Horizonte', 'MG', '30110062', '3133399000', -19.9316, -43.9436, 400, 200),
    ('Hospital Felício Rocho', 'Av. do Contorno, 9530', 'Belo Horizonte', 'MG', '30110934', '3132147100', -19.9327, -43.9425, 350, 170),

    -- Porto Alegre
    ('Hospital Moinhos de Vento', 'Rua Ramiro Barcelos, 910', 'Porto Alegre', 'RS', '90035001', '5130271777', -30.0312, -51.2112, 380, 190),
    ('Hospital São Lucas da PUCRS', 'Av. Ipiranga, 6690', 'Porto Alegre', 'RS', '90610000', '5133203000', -30.0605, -51.1709, 320, 150),

    -- Recife
    ('Real Hospital Português', 'Av. Agamenon Magalhães, 4760', 'Recife', 'PE', '52010075', '8134164444', -8.0476, -34.8950, 350, 160);

-- Insert more doctor schedules
INSERT INTO doctor_schedules (doctor_id, facility_id, day_of_week, start_time, end_time)
VALUES
    -- Doctor 1 - Works in multiple cities
    (1, 6, 3, '08:00', '18:00'),  -- Rio de Janeiro
    (1, 8, 4, '08:00', '18:00'),  -- Curitiba
    
    -- Doctor 2 - Specialized in two facilities
    (2, 7, 2, '09:00', '19:00'),  -- Rio de Janeiro
    (2, 7, 5, '09:00', '19:00'),
    
    -- Doctor 3 - Evening shifts
    (3, 9, 1, '14:00', '22:00'),  -- Belo Horizonte
    (3, 9, 3, '14:00', '22:00'),
    
    -- Doctor 4 - Morning shifts
    (4, 10, 2, '07:00', '15:00'), -- Belo Horizonte
    (4, 10, 4, '07:00', '15:00'),
    
    -- Doctor 5 - Weekend availability
    (5, 11, 6, '08:00', '16:00'), -- Porto Alegre
    (5, 11, 7, '08:00', '16:00');

-- Insert more administrator assignments
INSERT INTO administrator_facilities (administrator_id, facility_id)
VALUES
    -- Administrator 4 - Manages facilities in Rio
    (4, 6),
    (4, 7),
    
    -- Administrator 5 - Manages facilities in Curitiba
    (5, 8),
    (5, 9),
    
    -- Administrator 6 - Manages facilities in Belo Horizonte
    (6, 10),
    (6, 11),
    
    -- Administrator 7 - Manages facilities in Porto Alegre
    (7, 12),
    (7, 13),
    
    -- Administrator 8 - Manages facility in Recife
    (8, 14),
    
    -- Cross-city management for experienced administrators
    (1, 6),  -- Also manages in Rio
    (2, 8);  -- Also manages in Curitiba
