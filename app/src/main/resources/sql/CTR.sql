
SELECT(
    (SELECT CAST(COUNT(id) AS FLOAT) FROM click_log)
    /
    (SELECT CAST(COUNT(id) AS FLOAT) FROM impression_log)
     )