package org.myjaphoo.gui.db;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;
import com.jgoodies.validation.util.PropertyValidationSupport;
import org.apache.commons.lang.StringUtils;
import org.myjaphoo.model.dbconfig.DatabaseConfiguration;

/**
 * Validator for Database Configurations.
 */
public class DbConfigValidator implements Validator<DatabaseConfiguration> {

    public static final String NAME_MANDATORY = "Name is mandatory";

    @Override
    public ValidationResult validate(DatabaseConfiguration databaseConfiguration) {
        PropertyValidationSupport support = new PropertyValidationSupport(
                databaseConfiguration, "Database Configuration");
        if (StringUtils.isEmpty(databaseConfiguration.getName())) {
            support.addError("Name", NAME_MANDATORY);
        }
        return support.getResult();
    }
}
