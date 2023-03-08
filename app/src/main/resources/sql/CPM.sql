SELECT
(WITH totals AS(
SELECT SUM(impression_cost) AS total
FROM impression_log
UNION
SELECT SUM(click_cost) AS total
FROM click_log)
SELECT SUM(total) / 100
FROM totals)
/
(SELECT
(SELECT COUNT(age)
FROM impression_log)
/
1000)