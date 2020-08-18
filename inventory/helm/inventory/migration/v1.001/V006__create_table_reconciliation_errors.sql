CREATE TYPE t_reconciliation_error AS ENUM ('NO_NE_FOUND', 'UNKNOWN_MODEL', 'NO_MONITORING', 'NOT_DISCOVERED');

CREATE TABLE reconciliation_errors
(
    id              uuid PRIMARY KEY,
    task_id         uuid                   NOT NULL,
    object_identity VARCHAR(128),
    error_type      t_reconciliation_error NOT NULL,
    CONSTRAINT fk_rec_err_task FOREIGN KEY (task_id) REFERENCES reconciliation_tasks (id)
);