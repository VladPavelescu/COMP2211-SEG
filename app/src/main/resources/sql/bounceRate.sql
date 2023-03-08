SELECT
(SELECT CAST(COUNT(exit_date) AS REAL)
FROM server_log
WHERE unixepoch(exit_date) - unixepoch(entry_date) < 10 OR exit_date = "n/a")
/
(SELECT CAST(COUNT(id) AS REAL)
 FROM click_log);

