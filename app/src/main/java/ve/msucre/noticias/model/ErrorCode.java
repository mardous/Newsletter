package ve.msucre.noticias.model;

import androidx.annotation.StringRes;
import ve.msucre.noticias.R;

/**
 * @author Christians Martínez Alvarado
 */
public enum ErrorCode {

    apiKeyDisabled(R.string.error_apiKeyDisabled),
    apiKeyExhausted(R.string.error_apiKeyExhausted),
    parameterInvalid(R.string.error_parameterInvalid),
    parametersMissing(R.string.error_parametersMissing),
    rateLimited(R.string.error_rateLimited),
    unexpectedError(R.string.error_unexpectedError);

    @StringRes
    public int messageRes;

    ErrorCode(@StringRes int messageRes) {
        this.messageRes = messageRes;
    }
}
