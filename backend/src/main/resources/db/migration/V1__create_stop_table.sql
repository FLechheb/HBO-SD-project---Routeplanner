CREATE TABLE stop (
                      id BIGSERIAL PRIMARY KEY,
                      halte_nummer INTEGER NOT NULL,
                      halte_naam VARCHAR(100) NOT NULL,
                      aankomst_tijd TIME,
                      vertrek_tijd TIME,
                      voorziening VARCHAR(100)
);