SELECT COUNT(exit_date)
FROM server_log
WHERE unixepoch(exit_date) - unixepoch(entry_date) < 10 OR exit_date = "n/a";


