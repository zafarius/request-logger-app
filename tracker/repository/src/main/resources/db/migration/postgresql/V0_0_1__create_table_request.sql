CREATE TABLE "request"
(
    "id"                  UUID         NOT NULL,
    "request_id"          INTEGER      NOT NULL,
    "version"             BIGINT       NOT NULL DEFAULT 0,
    "created_id"          VARCHAR(100) NOT NULL,
    "created"             TIMESTAMP    NOT NULL,


    -- PK/UK Constraints
    CONSTRAINT "PK_account" PRIMARY KEY ("id"),
    CONSTRAINT "UK_request" UNIQUE ("request_id", "created_id")
);

