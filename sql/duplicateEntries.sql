select * from app.movieentry m1, app.movieentry m2 where m1.checksumcrc32 = m2.checksumcrc32 and m1.id <> m2.id order by m1.checksumcrc32, m2.canonicalpath