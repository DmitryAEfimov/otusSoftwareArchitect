CREATE TYPE t_task_statuses AS ENUM ('Pending', 'InProgress', 'Finished', 'Aborted', 'Error');

CREATE TABLE reconciliation_task
(
    id                  uuid PRIMARY KEY,
    discovery_report_id uuid,
    start_date          DATE            NOT NULL,
    end_date            DATE,
    status              t_task_statuses NOT NULL,
    status_detail       VARCHAR(1024)
);