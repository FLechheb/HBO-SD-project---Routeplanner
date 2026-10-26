-- Mock data: each physical facility is inserted and then linked to the platforms it serves (MAX("id") = the facility just inserted).

INSERT INTO "facility_type" ("name") VALUES
    ('ELEVATOR'),
    ('ESCALATOR'),
    ('STAIRS'),
    ('INFO_BOARD'),
    ('INFO_SPEAKER');

INSERT INTO "stop" ("gtfs_id", "name", "platform_code") VALUES
    ('NL:2992168', 'Amsterdam Centraal', '1'),
    ('NL:2992169', 'Amsterdam Centraal', '10'),
    ('NL:2992170', 'Amsterdam Centraal', '10a'),
    ('NL:2992171', 'Amsterdam Centraal', '10b'),
    ('NL:2992172', 'Amsterdam Centraal', '11'),
    ('NL:2992173', 'Amsterdam Centraal', '11a'),
    ('NL:2992174', 'Amsterdam Centraal', '11b'),
    ('NL:2992175', 'Amsterdam Centraal', '13'),
    ('NL:2992176', 'Amsterdam Centraal', '13a'),
    ('NL:2992177', 'Amsterdam Centraal', '13b'),
    ('NL:2992178', 'Amsterdam Centraal', '14'),
    ('NL:2992179', 'Amsterdam Centraal', '14a'),
    ('NL:2992180', 'Amsterdam Centraal', '14b'),
    ('NL:2992181', 'Amsterdam Centraal', '15'),
    ('NL:2992182', 'Amsterdam Centraal', '15a'),
    ('NL:2992183', 'Amsterdam Centraal', '15b'),
    ('NL:2992184', 'Amsterdam Centraal', '2'),
    ('NL:2992185', 'Amsterdam Centraal', '2a'),
    ('NL:2992186', 'Amsterdam Centraal', '2b'),
    ('NL:2992187', 'Amsterdam Centraal', '4'),
    ('NL:2992188', 'Amsterdam Centraal', '4a'),
    ('NL:2992189', 'Amsterdam Centraal', '4b'),
    ('NL:2992190', 'Amsterdam Centraal', '5'),
    ('NL:2992191', 'Amsterdam Centraal', '5a'),
    ('NL:2992192', 'Amsterdam Centraal', '5b'),
    ('NL:2992193', 'Amsterdam Centraal', '7'),
    ('NL:2992194', 'Amsterdam Centraal', '7a'),
    ('NL:2992195', 'Amsterdam Centraal', '7b'),
    ('NL:2992196', 'Amsterdam Centraal', '8'),
    ('NL:2992197', 'Amsterdam Centraal', '8a'),
    ('NL:2992198', 'Amsterdam Centraal', '8b'),
    ('NL:2993986', 'Utrecht Centraal', '1'),
    ('NL:2993987', 'Utrecht Centraal', '11'),
    ('NL:2993988', 'Utrecht Centraal', '11a'),
    ('NL:2993989', 'Utrecht Centraal', '11b'),
    ('NL:2993990', 'Utrecht Centraal', '12'),
    ('NL:2993991', 'Utrecht Centraal', '12a'),
    ('NL:2993992', 'Utrecht Centraal', '12b'),
    ('NL:2993993', 'Utrecht Centraal', '14'),
    ('NL:2993994', 'Utrecht Centraal', '14a'),
    ('NL:2993995', 'Utrecht Centraal', '14b'),
    ('NL:2993996', 'Utrecht Centraal', '15'),
    ('NL:2993997', 'Utrecht Centraal', '18'),
    ('NL:2993998', 'Utrecht Centraal', '18a'),
    ('NL:2993999', 'Utrecht Centraal', '18b'),
    ('NL:2994000', 'Utrecht Centraal', '19'),
    ('NL:2994001', 'Utrecht Centraal', '19a'),
    ('NL:2994002', 'Utrecht Centraal', '19b'),
    ('NL:2994003', 'Utrecht Centraal', '2'),
    ('NL:2994004', 'Utrecht Centraal', '20'),
    ('NL:2994005', 'Utrecht Centraal', '21'),
    ('NL:2994006', 'Utrecht Centraal', '3'),
    ('NL:2994007', 'Utrecht Centraal', '4'),
    ('NL:2994008', 'Utrecht Centraal', '4a'),
    ('NL:2994009', 'Utrecht Centraal', '4b'),
    ('NL:2994010', 'Utrecht Centraal', '5'),
    ('NL:2994011', 'Utrecht Centraal', '5a'),
    ('NL:2994012', 'Utrecht Centraal', '5b'),
    ('NL:2994013', 'Utrecht Centraal', '7'),
    ('NL:2994014', 'Utrecht Centraal', '7a'),
    ('NL:2994015', 'Utrecht Centraal', '7b'),
    ('NL:2994016', 'Utrecht Centraal', '8'),
    ('NL:2994017', 'Utrecht Centraal', '8a'),
    ('NL:2994018', 'Utrecht Centraal', '8b'),
    ('NL:2994019', 'Utrecht Centraal', '9'),
    ('NL:2994020', 'Utrecht Centraal', '9a'),
    ('NL:2994021', 'Utrecht Centraal', '9b'),
    ('NL:3065692', 'Amsterdam Centraal', '2aw');

-- Utrecht Centraal, spoor 1
INSERT INTO "facility" ("facility_type_id", "operational") SELECT "id", true FROM "facility_type" WHERE "name" = 'STAIRS';
INSERT INTO "stop_facility" ("stop_gtfs_id", "facility_id") SELECT "gtfs_id", (SELECT MAX("id") FROM "facility") FROM "stop" WHERE "name" = 'Utrecht Centraal' AND "platform_code" IN ('1');
INSERT INTO "facility" ("facility_type_id", "operational") SELECT "id", true FROM "facility_type" WHERE "name" = 'INFO_BOARD';
INSERT INTO "stop_facility" ("stop_gtfs_id", "facility_id") SELECT "gtfs_id", (SELECT MAX("id") FROM "facility") FROM "stop" WHERE "name" = 'Utrecht Centraal' AND "platform_code" IN ('1');

-- Utrecht Centraal, spoor 2/3
INSERT INTO "facility" ("facility_type_id", "operational") SELECT "id", true FROM "facility_type" WHERE "name" = 'ELEVATOR';
INSERT INTO "stop_facility" ("stop_gtfs_id", "facility_id") SELECT "gtfs_id", (SELECT MAX("id") FROM "facility") FROM "stop" WHERE "name" = 'Utrecht Centraal' AND "platform_code" IN ('2', '3');
INSERT INTO "facility" ("facility_type_id", "operational") SELECT "id", true FROM "facility_type" WHERE "name" = 'STAIRS';
INSERT INTO "stop_facility" ("stop_gtfs_id", "facility_id") SELECT "gtfs_id", (SELECT MAX("id") FROM "facility") FROM "stop" WHERE "name" = 'Utrecht Centraal' AND "platform_code" IN ('2', '3');

-- Utrecht Centraal, spoor 4/4a/4b
INSERT INTO "facility" ("facility_type_id", "operational") SELECT "id", true FROM "facility_type" WHERE "name" = 'ELEVATOR';
INSERT INTO "stop_facility" ("stop_gtfs_id", "facility_id") SELECT "gtfs_id", (SELECT MAX("id") FROM "facility") FROM "stop" WHERE "name" = 'Utrecht Centraal' AND "platform_code" IN ('4', '4a', '4b');
INSERT INTO "facility" ("facility_type_id", "operational") SELECT "id", true FROM "facility_type" WHERE "name" = 'ESCALATOR';
INSERT INTO "stop_facility" ("stop_gtfs_id", "facility_id") SELECT "gtfs_id", (SELECT MAX("id") FROM "facility") FROM "stop" WHERE "name" = 'Utrecht Centraal' AND "platform_code" IN ('4', '4a', '4b');
INSERT INTO "facility" ("facility_type_id", "operational") SELECT "id", true FROM "facility_type" WHERE "name" = 'STAIRS';
INSERT INTO "stop_facility" ("stop_gtfs_id", "facility_id") SELECT "gtfs_id", (SELECT MAX("id") FROM "facility") FROM "stop" WHERE "name" = 'Utrecht Centraal' AND "platform_code" IN ('4', '4a', '4b');

-- Utrecht Centraal, spoor 5/5a/5b/7/7a/7b
INSERT INTO "facility" ("facility_type_id", "operational") SELECT "id", true FROM "facility_type" WHERE "name" = 'ELEVATOR';
INSERT INTO "stop_facility" ("stop_gtfs_id", "facility_id") SELECT "gtfs_id", (SELECT MAX("id") FROM "facility") FROM "stop" WHERE "name" = 'Utrecht Centraal' AND "platform_code" IN ('5', '5a', '5b', '7', '7a', '7b');
INSERT INTO "facility" ("facility_type_id", "operational") SELECT "id", false FROM "facility_type" WHERE "name" = 'ELEVATOR';
INSERT INTO "stop_facility" ("stop_gtfs_id", "facility_id") SELECT "gtfs_id", (SELECT MAX("id") FROM "facility") FROM "stop" WHERE "name" = 'Utrecht Centraal' AND "platform_code" IN ('5', '5a', '5b', '7', '7a', '7b');
INSERT INTO "facility" ("facility_type_id", "operational") SELECT "id", true FROM "facility_type" WHERE "name" = 'ESCALATOR';
INSERT INTO "stop_facility" ("stop_gtfs_id", "facility_id") SELECT "gtfs_id", (SELECT MAX("id") FROM "facility") FROM "stop" WHERE "name" = 'Utrecht Centraal' AND "platform_code" IN ('5', '5a', '5b', '7', '7a', '7b');
INSERT INTO "facility" ("facility_type_id", "operational") SELECT "id", true FROM "facility_type" WHERE "name" = 'STAIRS';
INSERT INTO "stop_facility" ("stop_gtfs_id", "facility_id") SELECT "gtfs_id", (SELECT MAX("id") FROM "facility") FROM "stop" WHERE "name" = 'Utrecht Centraal' AND "platform_code" IN ('5', '5a', '5b', '7', '7a', '7b');

-- Utrecht Centraal, spoor 8/8a/8b/9/9a/9b
INSERT INTO "facility" ("facility_type_id", "operational") SELECT "id", true FROM "facility_type" WHERE "name" = 'ELEVATOR';
INSERT INTO "stop_facility" ("stop_gtfs_id", "facility_id") SELECT "gtfs_id", (SELECT MAX("id") FROM "facility") FROM "stop" WHERE "name" = 'Utrecht Centraal' AND "platform_code" IN ('8', '8a', '8b', '9', '9a', '9b');
INSERT INTO "facility" ("facility_type_id", "operational") SELECT "id", true FROM "facility_type" WHERE "name" = 'ESCALATOR';
INSERT INTO "stop_facility" ("stop_gtfs_id", "facility_id") SELECT "gtfs_id", (SELECT MAX("id") FROM "facility") FROM "stop" WHERE "name" = 'Utrecht Centraal' AND "platform_code" IN ('8', '8a', '8b', '9', '9a', '9b');
INSERT INTO "facility" ("facility_type_id", "operational") SELECT "id", true FROM "facility_type" WHERE "name" = 'STAIRS';
INSERT INTO "stop_facility" ("stop_gtfs_id", "facility_id") SELECT "gtfs_id", (SELECT MAX("id") FROM "facility") FROM "stop" WHERE "name" = 'Utrecht Centraal' AND "platform_code" IN ('8', '8a', '8b', '9', '9a', '9b');

-- Utrecht Centraal, spoor 11/11a/11b/12/12a/12b
INSERT INTO "facility" ("facility_type_id", "operational") SELECT "id", true FROM "facility_type" WHERE "name" = 'ELEVATOR';
INSERT INTO "stop_facility" ("stop_gtfs_id", "facility_id") SELECT "gtfs_id", (SELECT MAX("id") FROM "facility") FROM "stop" WHERE "name" = 'Utrecht Centraal' AND "platform_code" IN ('11', '11a', '11b', '12', '12a', '12b');
INSERT INTO "facility" ("facility_type_id", "operational") SELECT "id", true FROM "facility_type" WHERE "name" = 'ESCALATOR';
INSERT INTO "stop_facility" ("stop_gtfs_id", "facility_id") SELECT "gtfs_id", (SELECT MAX("id") FROM "facility") FROM "stop" WHERE "name" = 'Utrecht Centraal' AND "platform_code" IN ('11', '11a', '11b', '12', '12a', '12b');
INSERT INTO "facility" ("facility_type_id", "operational") SELECT "id", true FROM "facility_type" WHERE "name" = 'STAIRS';
INSERT INTO "stop_facility" ("stop_gtfs_id", "facility_id") SELECT "gtfs_id", (SELECT MAX("id") FROM "facility") FROM "stop" WHERE "name" = 'Utrecht Centraal' AND "platform_code" IN ('11', '11a', '11b', '12', '12a', '12b');

-- Utrecht Centraal, spoor 14/14a/14b/15
INSERT INTO "facility" ("facility_type_id", "operational") SELECT "id", true FROM "facility_type" WHERE "name" = 'ELEVATOR';
INSERT INTO "stop_facility" ("stop_gtfs_id", "facility_id") SELECT "gtfs_id", (SELECT MAX("id") FROM "facility") FROM "stop" WHERE "name" = 'Utrecht Centraal' AND "platform_code" IN ('14', '14a', '14b', '15');
INSERT INTO "facility" ("facility_type_id", "operational") SELECT "id", true FROM "facility_type" WHERE "name" = 'STAIRS';
INSERT INTO "stop_facility" ("stop_gtfs_id", "facility_id") SELECT "gtfs_id", (SELECT MAX("id") FROM "facility") FROM "stop" WHERE "name" = 'Utrecht Centraal' AND "platform_code" IN ('14', '14a', '14b', '15');

-- Utrecht Centraal, spoor 18/18a/18b/19/19a/19b
INSERT INTO "facility" ("facility_type_id", "operational") SELECT "id", true FROM "facility_type" WHERE "name" = 'ELEVATOR';
INSERT INTO "stop_facility" ("stop_gtfs_id", "facility_id") SELECT "gtfs_id", (SELECT MAX("id") FROM "facility") FROM "stop" WHERE "name" = 'Utrecht Centraal' AND "platform_code" IN ('18', '18a', '18b', '19', '19a', '19b');
INSERT INTO "facility" ("facility_type_id", "operational") SELECT "id", true FROM "facility_type" WHERE "name" = 'ESCALATOR';
INSERT INTO "stop_facility" ("stop_gtfs_id", "facility_id") SELECT "gtfs_id", (SELECT MAX("id") FROM "facility") FROM "stop" WHERE "name" = 'Utrecht Centraal' AND "platform_code" IN ('18', '18a', '18b', '19', '19a', '19b');
INSERT INTO "facility" ("facility_type_id", "operational") SELECT "id", true FROM "facility_type" WHERE "name" = 'STAIRS';
INSERT INTO "stop_facility" ("stop_gtfs_id", "facility_id") SELECT "gtfs_id", (SELECT MAX("id") FROM "facility") FROM "stop" WHERE "name" = 'Utrecht Centraal' AND "platform_code" IN ('18', '18a', '18b', '19', '19a', '19b');

-- Utrecht Centraal, spoor 20/21
INSERT INTO "facility" ("facility_type_id", "operational") SELECT "id", true FROM "facility_type" WHERE "name" = 'ELEVATOR';
INSERT INTO "stop_facility" ("stop_gtfs_id", "facility_id") SELECT "gtfs_id", (SELECT MAX("id") FROM "facility") FROM "stop" WHERE "name" = 'Utrecht Centraal' AND "platform_code" IN ('20', '21');
INSERT INTO "facility" ("facility_type_id", "operational") SELECT "id", true FROM "facility_type" WHERE "name" = 'STAIRS';
INSERT INTO "stop_facility" ("stop_gtfs_id", "facility_id") SELECT "gtfs_id", (SELECT MAX("id") FROM "facility") FROM "stop" WHERE "name" = 'Utrecht Centraal' AND "platform_code" IN ('20', '21');

-- Amsterdam Centraal, spoor 1/2/2a/2aw/2b
INSERT INTO "facility" ("facility_type_id", "operational") SELECT "id", true FROM "facility_type" WHERE "name" = 'ELEVATOR';
INSERT INTO "stop_facility" ("stop_gtfs_id", "facility_id") SELECT "gtfs_id", (SELECT MAX("id") FROM "facility") FROM "stop" WHERE "name" = 'Amsterdam Centraal' AND "platform_code" IN ('1', '2', '2a', '2aw', '2b');
INSERT INTO "facility" ("facility_type_id", "operational") SELECT "id", true FROM "facility_type" WHERE "name" = 'STAIRS';
INSERT INTO "stop_facility" ("stop_gtfs_id", "facility_id") SELECT "gtfs_id", (SELECT MAX("id") FROM "facility") FROM "stop" WHERE "name" = 'Amsterdam Centraal' AND "platform_code" IN ('1', '2', '2a', '2aw', '2b');

-- Amsterdam Centraal, spoor 4/4a/4b/5/5a/5b
INSERT INTO "facility" ("facility_type_id", "operational") SELECT "id", true FROM "facility_type" WHERE "name" = 'ELEVATOR';
INSERT INTO "stop_facility" ("stop_gtfs_id", "facility_id") SELECT "gtfs_id", (SELECT MAX("id") FROM "facility") FROM "stop" WHERE "name" = 'Amsterdam Centraal' AND "platform_code" IN ('4', '4a', '4b', '5', '5a', '5b');
INSERT INTO "facility" ("facility_type_id", "operational") SELECT "id", true FROM "facility_type" WHERE "name" = 'ESCALATOR';
INSERT INTO "stop_facility" ("stop_gtfs_id", "facility_id") SELECT "gtfs_id", (SELECT MAX("id") FROM "facility") FROM "stop" WHERE "name" = 'Amsterdam Centraal' AND "platform_code" IN ('4', '4a', '4b', '5', '5a', '5b');
INSERT INTO "facility" ("facility_type_id", "operational") SELECT "id", true FROM "facility_type" WHERE "name" = 'STAIRS';
INSERT INTO "stop_facility" ("stop_gtfs_id", "facility_id") SELECT "gtfs_id", (SELECT MAX("id") FROM "facility") FROM "stop" WHERE "name" = 'Amsterdam Centraal' AND "platform_code" IN ('4', '4a', '4b', '5', '5a', '5b');

-- Amsterdam Centraal, spoor 7/7a/7b/8/8a/8b
INSERT INTO "facility" ("facility_type_id", "operational") SELECT "id", false FROM "facility_type" WHERE "name" = 'ELEVATOR';
INSERT INTO "stop_facility" ("stop_gtfs_id", "facility_id") SELECT "gtfs_id", (SELECT MAX("id") FROM "facility") FROM "stop" WHERE "name" = 'Amsterdam Centraal' AND "platform_code" IN ('7', '7a', '7b', '8', '8a', '8b');
INSERT INTO "facility" ("facility_type_id", "operational") SELECT "id", true FROM "facility_type" WHERE "name" = 'ESCALATOR';
INSERT INTO "stop_facility" ("stop_gtfs_id", "facility_id") SELECT "gtfs_id", (SELECT MAX("id") FROM "facility") FROM "stop" WHERE "name" = 'Amsterdam Centraal' AND "platform_code" IN ('7', '7a', '7b', '8', '8a', '8b');
INSERT INTO "facility" ("facility_type_id", "operational") SELECT "id", true FROM "facility_type" WHERE "name" = 'STAIRS';
INSERT INTO "stop_facility" ("stop_gtfs_id", "facility_id") SELECT "gtfs_id", (SELECT MAX("id") FROM "facility") FROM "stop" WHERE "name" = 'Amsterdam Centraal' AND "platform_code" IN ('7', '7a', '7b', '8', '8a', '8b');

-- Amsterdam Centraal, spoor 10/10a/10b/11/11a/11b
INSERT INTO "facility" ("facility_type_id", "operational") SELECT "id", true FROM "facility_type" WHERE "name" = 'ELEVATOR';
INSERT INTO "stop_facility" ("stop_gtfs_id", "facility_id") SELECT "gtfs_id", (SELECT MAX("id") FROM "facility") FROM "stop" WHERE "name" = 'Amsterdam Centraal' AND "platform_code" IN ('10', '10a', '10b', '11', '11a', '11b');
INSERT INTO "facility" ("facility_type_id", "operational") SELECT "id", true FROM "facility_type" WHERE "name" = 'ESCALATOR';
INSERT INTO "stop_facility" ("stop_gtfs_id", "facility_id") SELECT "gtfs_id", (SELECT MAX("id") FROM "facility") FROM "stop" WHERE "name" = 'Amsterdam Centraal' AND "platform_code" IN ('10', '10a', '10b', '11', '11a', '11b');
INSERT INTO "facility" ("facility_type_id", "operational") SELECT "id", true FROM "facility_type" WHERE "name" = 'STAIRS';
INSERT INTO "stop_facility" ("stop_gtfs_id", "facility_id") SELECT "gtfs_id", (SELECT MAX("id") FROM "facility") FROM "stop" WHERE "name" = 'Amsterdam Centraal' AND "platform_code" IN ('10', '10a', '10b', '11', '11a', '11b');

-- Amsterdam Centraal, spoor 13/13a/13b/14/14a/14b
INSERT INTO "facility" ("facility_type_id", "operational") SELECT "id", true FROM "facility_type" WHERE "name" = 'ELEVATOR';
INSERT INTO "stop_facility" ("stop_gtfs_id", "facility_id") SELECT "gtfs_id", (SELECT MAX("id") FROM "facility") FROM "stop" WHERE "name" = 'Amsterdam Centraal' AND "platform_code" IN ('13', '13a', '13b', '14', '14a', '14b');
INSERT INTO "facility" ("facility_type_id", "operational") SELECT "id", true FROM "facility_type" WHERE "name" = 'ESCALATOR';
INSERT INTO "stop_facility" ("stop_gtfs_id", "facility_id") SELECT "gtfs_id", (SELECT MAX("id") FROM "facility") FROM "stop" WHERE "name" = 'Amsterdam Centraal' AND "platform_code" IN ('13', '13a', '13b', '14', '14a', '14b');
INSERT INTO "facility" ("facility_type_id", "operational") SELECT "id", true FROM "facility_type" WHERE "name" = 'STAIRS';
INSERT INTO "stop_facility" ("stop_gtfs_id", "facility_id") SELECT "gtfs_id", (SELECT MAX("id") FROM "facility") FROM "stop" WHERE "name" = 'Amsterdam Centraal' AND "platform_code" IN ('13', '13a', '13b', '14', '14a', '14b');

-- Amsterdam Centraal, spoor 15/15a/15b
INSERT INTO "facility" ("facility_type_id", "operational") SELECT "id", true FROM "facility_type" WHERE "name" = 'ELEVATOR';
INSERT INTO "stop_facility" ("stop_gtfs_id", "facility_id") SELECT "gtfs_id", (SELECT MAX("id") FROM "facility") FROM "stop" WHERE "name" = 'Amsterdam Centraal' AND "platform_code" IN ('15', '15a', '15b');
INSERT INTO "facility" ("facility_type_id", "operational") SELECT "id", true FROM "facility_type" WHERE "name" = 'STAIRS';
INSERT INTO "stop_facility" ("stop_gtfs_id", "facility_id") SELECT "gtfs_id", (SELECT MAX("id") FROM "facility") FROM "stop" WHERE "name" = 'Amsterdam Centraal' AND "platform_code" IN ('15', '15a', '15b');
