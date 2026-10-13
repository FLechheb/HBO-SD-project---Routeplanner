CREATE TABLE "special_need" (
    "type" VARCHAR(50) PRIMARY KEY,
    "extra_time" BOOLEAN NOT NULL,
    "seating" BOOLEAN NOT NULL,
    "level_floor" BOOLEAN NOT NULL,
    "visual_aid" BOOLEAN NOT NULL,
    "auditory_aid" BOOLEAN NOT NULL
);
