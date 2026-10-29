DROP TABLE "stop_facility";
DROP TABLE "facility";

CREATE TABLE "facility_type" (
    "id" SERIAL PRIMARY KEY,
    "name" VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE "facility" (
    "id" SERIAL PRIMARY KEY,
    "facility_type_id" INTEGER NOT NULL REFERENCES "facility_type" ("id"),
    "operational" BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE "stop_facility" (
    "stop_gtfs_id" VARCHAR(64) NOT NULL REFERENCES "stop" ("gtfs_id") ON DELETE CASCADE,
    "facility_id" INTEGER NOT NULL REFERENCES "facility" ("id") ON DELETE CASCADE,
    PRIMARY KEY ("stop_gtfs_id", "facility_id")
);
