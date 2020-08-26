package ve.msucre.noticias.api.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import ve.msucre.noticias.api.request.RequestBase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Christians Martínez Alvarado
 */
public class Sources implements RequestBase {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("sources")
    @Expose
    private List<Source> sources;

    @NonNull
    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Nullable
    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @NonNull
    public List<Source> getSources() {
        if (sources == null) {
            return new ArrayList<>();
        }
        return sources;
    }

    public void setSources(List<Source> sources) {
        this.sources = sources;
    }
}
