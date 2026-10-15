DROP TABLE "stop";

CREATE TABLE "stop" (
    "gtfs_id" VARCHAR(64) PRIMARY KEY,
    "name" VARCHAR(100) NOT NULL,
    "platform_code" VARCHAR(10)
);

CREATE TABLE "facility" (
    "id" SERIAL PRIMARY KEY,
    "name" VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE "stop_facility" (
    "stop_gtfs_id" VARCHAR(64) NOT NULL REFERENCES "stop" ("gtfs_id") ON DELETE CASCADE,
    "facility_id" INTEGER NOT NULL REFERENCES "facility" ("id"),
    PRIMARY KEY ("stop_gtfs_id", "facility_id")
);
