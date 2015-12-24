update app.MOVIEENTRY set DUPLETTE = 0;

update app.MOVIEENTRY m set m.duplette = 1 where m.id in (
select id from (
select m1.id as id , m1.checksumcrc32 from app.movieentry m1, app.movieentry m2 where m1.checksumcrc32 = m2.checksumcrc32 and m1.id <> m2.id 
except
select min(m1.id) as id, m1.checksumcrc32 from app.movieentry m1, app.movieentry m2 where m1.checksumcrc32 = m2.checksumcrc32 and m1.id <> m2.id  group by m1.checksumcrc32) as dups)