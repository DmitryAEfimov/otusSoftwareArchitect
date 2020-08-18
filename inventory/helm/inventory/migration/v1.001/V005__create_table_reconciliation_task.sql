CREATE TYPE t_task_statuses AS ENUM ('Pending', 'InProgress', 'Finished', 'Aborted', 'Error');

CREATE TABLE reconciliation_tasks
(
    id                  uuid PRIMARY KEY,
    discovery_report_id uuid,
    start_date          TIMESTAMP            NOT NULL,
    end_date            TIMESTAMP,
    status              t_task_statuses NOT NULL,
    status_detail       VARCHAR(1024)
);