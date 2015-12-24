package org.myjaphoo.model.dbcompare;

import org.apache.commons.lang.ObjectUtils;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.Rating;
import org.myjaphoo.model.db.exif.ExifData;

/**
 * Movie Entry compare aspects.
 */
public enum MovieEntryCompareAspect implements ComparisonAspect<MovieEntry> {


    NAME {
        @Override
        public Object getAspect(MovieEntry t) {
            return t.getName();
        }

        @Override
        public void setAspect(MovieEntry t, Object aspect) {
            t.setName((String) aspect);
        }
    },
    CANONICALDIR {
        @Override
        public Object getAspect(MovieEntry t) {
            return t.getCanonicalDir();
        }

        @Override
        public void setAspect(MovieEntry t, Object aspect) {
            t.setCanonicalDir((String) aspect);
        }

    }, FILELENGTH {
        @Override
        public Object getAspect(MovieEntry t) {
            return t.getFileLength();
        }

        @Override
        public void setAspect(MovieEntry t, Object aspect) {
            t.setFileLength((Long) aspect);
        }
    }, CHECKSUMCRC32 {
        @Override
        public Object getAspect(MovieEntry t) {
            return t.getChecksumCRC32();
        }

        @Override
        public void setAspect(MovieEntry t, Object aspect) {
            t.setChecksumCRC32((Long) aspect);
        }
    }, RATING {
        @Override
        public Object getAspect(MovieEntry t) {
            return t.getRating();
        }

        @Override
        public void setAspect(MovieEntry t, Object aspect) {
            t.setRating((Rating) aspect);
        }
    }, TITLE {
        @Override
        public Object getAspect(MovieEntry t) {
            return t.getTitle();
        }

        @Override
        public void setAspect(MovieEntry t, Object aspect) {
            t.setTitle((String) aspect);
        }
    }, COMMENT {
        @Override
        public Object getAspect(MovieEntry t) {
            return t.getComment();
        }

        @Override
        public void setAspect(MovieEntry t, Object aspect) {
            t.setComment((String) aspect);
        }
    }, EXIFDATA {
        @Override
        public Object getAspect(MovieEntry t) {
            return t.getExifData();
        }

        @Override
        public void setAspect(MovieEntry t, Object aspect) {
            t.setExifData((ExifData) aspect);
        }
    };
    @Override
    public String getName() {
        return name();
    }
}
