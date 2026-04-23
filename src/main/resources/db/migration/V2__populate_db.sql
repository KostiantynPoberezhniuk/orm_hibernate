INSERT INTO planet (id, name) VALUES
    ('MARS',  'Mars'),
    ('VEN',   'Venus'),
    ('EARTH', 'Earth'),
    ('JUP',   'Jupiter'),
    ('SAT',   'Saturn');

INSERT INTO client (name) VALUES
    ('Alice Johnson'),
    ('Bob Smith'),
    ('Charlie Brown'),
    ('Diana Prince'),
    ('Evan Wright'),
    ('Fiona Black'),
    ('George Miller'),
    ('Hannah White'),
    ('Ian Murphy'),
    ('Julia Green');

INSERT INTO ticket (created_at, client_id, from_planet_id, to_planet_id) VALUES
    (TIMESTAMP WITH TIME ZONE '2026-01-10 08:00:00+00',  1, 'EARTH', 'MARS'),
    (TIMESTAMP WITH TIME ZONE '2026-01-12 09:30:00+00',  2, 'MARS',  'VEN'),
    (TIMESTAMP WITH TIME ZONE '2026-01-15 11:15:00+00',  3, 'VEN',   'EARTH'),
    (TIMESTAMP WITH TIME ZONE '2026-01-20 14:45:00+00',  4, 'EARTH', 'JUP'),
    (TIMESTAMP WITH TIME ZONE '2026-01-22 07:00:00+00',  5, 'JUP',   'SAT'),
    (TIMESTAMP WITH TIME ZONE '2026-02-01 10:00:00+00',  6, 'SAT',   'MARS'),
    (TIMESTAMP WITH TIME ZONE '2026-02-05 12:30:00+00',  7, 'MARS',  'EARTH'),
    (TIMESTAMP WITH TIME ZONE '2026-02-08 15:00:00+00',  8, 'EARTH', 'VEN'),
    (TIMESTAMP WITH TIME ZONE '2026-02-11 09:45:00+00',  9, 'VEN',   'JUP'),
    (TIMESTAMP WITH TIME ZONE '2026-02-14 18:20:00+00', 10, 'JUP',   'MARS');