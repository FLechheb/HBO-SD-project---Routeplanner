ALTER TABLE stop
    RENAME COLUMN halte_nummer TO stop_number;

ALTER TABLE stop
    RENAME COLUMN halte_naam TO stop_name;

ALTER TABLE stop
    RENAME COLUMN aankomst_tijd TO arrival_time;

ALTER TABLE stop
    RENAME COLUMN vertrek_tijd TO departure_time;

ALTER TABLE stop
    RENAME COLUMN voorziening TO facility;