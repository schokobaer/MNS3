package at.ac.tuwien.mns.group3.mnsg3e3.persistence;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import at.ac.tuwien.mns.group3.mnsg3e3.model.Report;

import java.util.List;

public class ReportRepository {

    private ReportDao mReportDao;
    private LiveData<List<Report>> mAllReports;

    public ReportRepository(AppDatabase db) {
        mReportDao = db.reportDao();
        mAllReports = mReportDao.getAll();
    }

    public LiveData<List<Report>> getAllReports() {
        return mAllReports;
    }

    public void insert (Report report) {
        new insertAsyncTask(mReportDao).execute(report);
    }

    private static class insertAsyncTask extends AsyncTask<Report, Void, Void> {

        private ReportDao mAsyncTaskDao;

        insertAsyncTask(ReportDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Report... params) {
            mAsyncTaskDao.insertAll(params[0]);
            return null;
        }
    }

    public void delete (Report report) {
        new deleteAsyncTask(mReportDao).execute(report);
    }

    private static class deleteAsyncTask extends AsyncTask<Report, Void, Void> {

        private ReportDao mAsyncTaskDao;

        deleteAsyncTask(ReportDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Report... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }
}
