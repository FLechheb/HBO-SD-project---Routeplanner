-- Provisional travel-assistance presets; refine applied values with a new migration.
INSERT INTO "special_need" ("type", "extra_time", "seating", "level_floor", "visual_aid", "auditory_aid") VALUES
    ('DEAF', false, false, false, true, false),
    ('BLIND', true, false, false, false, true),
    ('WHEELCHAIR_BOUND', true, false, true, false, false),
    ('ELDERLY', true, true, true, false, false),
    ('PHYSICAL_IMPAIRED', true, true, true, false, false);
