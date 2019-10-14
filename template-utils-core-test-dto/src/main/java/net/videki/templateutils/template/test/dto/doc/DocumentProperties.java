package net.videki.templateutils.template.test.dto.doc;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Levente Ban
 * @since v1.0
 *
 * <p>Sample print cover page data</p>
 */
public class DocumentProperties {
    /** Officer login */
    private String login;

    /** Document generation date */
    private LocalDateTime generationDate;

    /** DMS url */
    private String dmsUrl;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public LocalDateTime getGenerationDate() {
        return generationDate;
    }

    public void setGenerationDate(LocalDateTime generationDate) {
        this.generationDate = generationDate;
    }

    public String getDmsUrl() {
        return dmsUrl;
    }

    public void setDmsUrl(String dmsUrl) {
        this.dmsUrl = dmsUrl;
    }
}
