package karrel.com.multipartasynctask;

import android.os.AsyncTask;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by 이주영 on 2016-12-01.
 */

public abstract class MultipartAsyncTask extends AsyncTask<Void, Void, Void> {
    protected String mRequestURL;
    private String[] mFormFields;
    private Object[] mFileParts;
    private MultipartUploader multipartUploader;
    private List<String> mResultList;

    private String headerKey;
    private String headerValue;

    protected abstract void onResult(List<String> resultList);

    public MultipartAsyncTask(String requestURL, String headerKey, String headerValue) {
        mRequestURL = requestURL;
        setHeader(headerKey, headerValue);
    }

    private void setHeader(String headerKey, String headerValue) {
        this.headerKey = headerKey;
        this.headerValue = headerValue;
    }

    /**
     * key와 value를 넣어주면된다.
     */
    protected void setFormFields(String... formField) {
        mFormFields = formField;
    }

    /**
     * key와 value를 넣어주면된다.
     * key는 String타입이어야하며, value는 File타입 이어야한다.
     */
    protected void setFileParts(Object... fileParts) {
        mFileParts = fileParts;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            multipartUploader = new MultipartUploader(mRequestURL, "UTF-8", headerKey, headerValue);

            if (mFormFields != null) {
                for (int i = 0; i < mFormFields.length; i += 2) {
                    // 폼필드를 넣는다 key는 String타입이며 value도 String 타입이다.
                    multipartUploader.addFormField(mFormFields[i], mFormFields[i + 1]);
                }
            }

            if (mFileParts != null) {
                for (int i = 0; i < mFileParts.length; i += 2) {
                    // 파일을 넣는다. key는 String타입이며 value는 File타입이다.
                    multipartUploader.addFilePart((String) mFileParts[i], (File) mFileParts[i + 1]);
                }
            }

            mResultList = multipartUploader.finish();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        onResult(mResultList);
    }
}
