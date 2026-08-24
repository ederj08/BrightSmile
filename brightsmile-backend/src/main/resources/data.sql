
-- =============================================
-- SERVICIOS DENTALES
-- =============================================
INSERT INTO servicio (nombre, descripcion, duracion_minutos, precio, activo)
SELECT 'Initial Assessment', 'Dental evaluation and treatment plan consultation', 30, 30.00, true
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Initial Assessment');

INSERT INTO servicio (nombre, descripcion, duracion_minutos, precio, activo)
SELECT 'Dental Cleaning', 'Professional teeth cleaning and plaque removal', 60, 120.00, true
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Dental Cleaning');

INSERT INTO servicio (nombre, descripcion, duracion_minutos, precio, activo)
SELECT 'Teeth Whitening', 'Professional in-office teeth whitening treatment', 90, 299.00, true
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Teeth Whitening');

INSERT INTO servicio (nombre, descripcion, duracion_minutos, precio, activo)
SELECT 'Dental X-Ray', 'Full mouth digital X-ray examination', 30, 85.00, true
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Dental X-Ray');

INSERT INTO servicio (nombre, descripcion, duracion_minutos, precio, activo)
SELECT 'Tooth Extraction', 'Simple tooth extraction procedure', 45, 150.00, true
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Tooth Extraction');

INSERT INTO servicio (nombre, descripcion, duracion_minutos, precio, activo)
SELECT 'Dental Filling', 'Composite resin tooth filling', 60, 175.00, true
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Dental Filling');

INSERT INTO servicio (nombre, descripcion, duracion_minutos, precio, activo)
SELECT 'Root Canal', 'Root canal treatment for infected teeth', 120, 850.00, true
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Root Canal');

INSERT INTO servicio (nombre, descripcion, duracion_minutos, precio, activo)
SELECT 'Orthodontic Consultation', 'Initial braces and aligners consultation', 60, 200.00, true
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Orthodontic Consultation');

INSERT INTO servicio (nombre, descripcion, duracion_minutos, precio, activo)
SELECT 'Dental Crown', 'Porcelain dental crown placement', 90, 950.00, true
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Dental Crown');

-- =============================================
-- HORARIOS DISPONIBLES
-- =============================================

-- LUNES (1)
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 1, '08:00', '08:30', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 1 AND hora_inicio = '08:00');
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 1, '08:30', '09:00', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 1 AND hora_inicio = '08:30');
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 1, '09:00', '09:30', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 1 AND hora_inicio = '09:00');
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 1, '09:30', '10:00', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 1 AND hora_inicio = '09:30');
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 1, '10:00', '10:30', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 1 AND hora_inicio = '10:00');
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 1, '10:30', '11:00', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 1 AND hora_inicio = '10:30');
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 1, '11:00', '11:30', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 1 AND hora_inicio = '11:00');
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 1, '14:00', '14:30', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 1 AND hora_inicio = '14:00');
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 1, '14:30', '15:00', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 1 AND hora_inicio = '14:30');
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 1, '15:00', '15:30', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 1 AND hora_inicio = '15:00');
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 1, '15:30', '16:00', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 1 AND hora_inicio = '15:30');
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 1, '16:00', '16:30', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 1 AND hora_inicio = '16:00');
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 1, '16:30', '17:00', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 1 AND hora_inicio = '16:30');

-- MARTES (2)
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 2, '08:00', '08:30', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 2 AND hora_inicio = '08:00');
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 2, '08:30', '09:00', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 2 AND hora_inicio = '08:30');
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 2, '09:00', '09:30', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 2 AND hora_inicio = '09:00');
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 2, '09:30', '10:00', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 2 AND hora_inicio = '09:30');
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 2, '10:00', '10:30', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 2 AND hora_inicio = '10:00');
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 2, '14:00', '14:30', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 2 AND hora_inicio = '14:00');
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 2, '14:30', '15:00', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 2 AND hora_inicio = '14:30');
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 2, '15:00', '15:30', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 2 AND hora_inicio = '15:00');
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 2, '15:30', '16:00', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 2 AND hora_inicio = '15:30');
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 2, '16:00', '16:30', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 2 AND hora_inicio = '16:00');

-- MIÉRCOLES (3)
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 3, '08:00', '08:30', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 3 AND hora_inicio = '08:00');
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 3, '09:00', '09:30', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 3 AND hora_inicio = '09:00');
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 3, '10:00', '10:30', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 3 AND hora_inicio = '10:00');
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 3, '11:00', '11:30', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 3 AND hora_inicio = '11:00');
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 3, '14:00', '14:30', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 3 AND hora_inicio = '14:00');
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 3, '15:00', '15:30', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 3 AND hora_inicio = '15:00');
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 3, '16:00', '16:30', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 3 AND hora_inicio = '16:00');

-- JUEVES (4)
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 4, '08:00', '08:30', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 4 AND hora_inicio = '08:00');
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 4, '09:00', '09:30', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 4 AND hora_inicio = '09:00');
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 4, '10:00', '10:30', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 4 AND hora_inicio = '10:00');
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 4, '14:00', '14:30', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 4 AND hora_inicio = '14:00');
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 4, '15:00', '15:30', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 4 AND hora_inicio = '15:00');
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 4, '16:00', '16:30', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 4 AND hora_inicio = '16:00');

-- VIERNES (5)
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 5, '08:00', '08:30', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 5 AND hora_inicio = '08:00');
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 5, '09:00', '09:30', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 5 AND hora_inicio = '09:00');
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 5, '10:00', '10:30', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 5 AND hora_inicio = '10:00');
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 5, '14:00', '14:30', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 5 AND hora_inicio = '14:00');
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 5, '15:00', '15:30', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 5 AND hora_inicio = '15:00');

-- SÁBADO (6)
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 6, '09:00', '09:30', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 6 AND hora_inicio = '09:00');
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 6, '09:30', '10:00', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 6 AND hora_inicio = '09:30');
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 6, '10:00', '10:30', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 6 AND hora_inicio = '10:00');
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 6, '10:30', '11:00', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 6 AND hora_inicio = '10:30');
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 6, '11:00', '11:30', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 6 AND hora_inicio = '11:00');
INSERT INTO horario_disponible (dia_semana, hora_inicio, hora_fin, disponible) SELECT 6, '11:30', '12:00', true WHERE NOT EXISTS (SELECT 1 FROM horario_disponible WHERE dia_semana = 6 AND hora_inicio = '11:30');