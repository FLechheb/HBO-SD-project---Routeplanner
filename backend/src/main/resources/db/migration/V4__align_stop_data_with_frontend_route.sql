UPDATE stop
SET stop_name = 'Utrecht Centraal',
    arrival_time = TIME '10:00:00',
    departure_time = TIME '10:02:00',
    facility = 'lift'
WHERE stop_number = 1;

UPDATE stop
SET stop_name = 'Utrecht Overvecht',
    arrival_time = TIME '10:07:00',
    departure_time = TIME '10:08:00',
    facility = 'roltrap'
WHERE stop_number = 2;

UPDATE stop
SET stop_name = 'Maarssen',
    arrival_time = TIME '10:13:00',
    departure_time = TIME '10:14:00',
    facility = 'hellingbaan'
WHERE stop_number = 3;

UPDATE stop
SET stop_name = 'Breukelen',
    arrival_time = TIME '10:20:00',
    departure_time = TIME '10:21:00',
    facility = 'lift'
WHERE stop_number = 4;

UPDATE stop
SET stop_name = 'Abcoude',
    arrival_time = TIME '10:29:00',
    departure_time = TIME '10:30:00',
    facility = 'geleidelijn'
WHERE stop_number = 5;

UPDATE stop
SET stop_name = 'Amsterdam Holendrecht',
    arrival_time = TIME '10:36:00',
    departure_time = TIME '10:37:00',
    facility = 'roltrap'
WHERE stop_number = 6;

UPDATE stop
SET stop_name = 'Amsterdam Zuid',
    arrival_time = TIME '10:44:00',
    departure_time = TIME '10:46:00',
    facility = 'lift'
WHERE stop_number = 7;

UPDATE stop
SET stop_name = 'Amsterdam RAI',
    arrival_time = TIME '10:50:00',
    departure_time = TIME '10:51:00',
    facility = 'hellingbaan'
WHERE stop_number = 8;

UPDATE stop
SET stop_name = 'Amsterdam Amstel',
    arrival_time = TIME '10:57:00',
    departure_time = TIME '10:59:00',
    facility = 'geleidelijn'
WHERE stop_number = 9;

UPDATE stop
SET stop_name = 'Amsterdam Centraal',
    arrival_time = TIME '11:06:00',
    departure_time = TIME '11:08:00',
    facility = 'lift'
WHERE stop_number = 10;