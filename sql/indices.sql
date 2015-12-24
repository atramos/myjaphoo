create unique index canpathindx on app.movieentry(canonicalpath);
create unique index toknameidx on app.token(name);
create index me_flidx on app.movieentry(filelength);
create index me_chksmidx on app.movieentry(checksumcrc32);

