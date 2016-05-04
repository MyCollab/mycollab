package ch.qos.cal10n;

import ch.qos.cal10n.util.AnnotationExtractorViaEnumClass;
import ch.qos.cal10n.util.CAL10NBundleExt;
import ch.qos.cal10n.util.CAL10NBundleFinderByClassloaderExt;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
public class MessageConveyorExt implements IMessageConveyor {
    static final ResourceLoader resourceLoader = new ResourceLoader();
    final Locale locale;
    final Map<String, CAL10NBundleExt> cache = new ConcurrentHashMap<>();

    /**
     * The {@link Locale} associated with this instance.
     *
     * @param locale the Locale which this conveyor targets
     */
    public MessageConveyorExt(Locale locale) {
        this.locale = locale;
    }

    /**
     * Given an enum as key, find the resource bundle corresponding to this
     * locale and return the message corresponding to the key passed as
     * parameter (internationalized per this locale).
     *
     * The name of the resource bundle is defined via the {@link BaseName}
     * annotation whereas the locale is specified in this MessageConveyor
     * instance's constructor.
     *
     * @param key an enum instance used as message key
     */
    public <E extends Enum<?>> String getMessage(E key, Object... args)
            throws MessageConveyorException {

        Class<? extends Enum<?>> declaringClass = key.getDeclaringClass();

        String declaringClassName = declaringClass.getName();
        CAL10NBundleExt rb = cache.get(declaringClassName);
        if (rb == null || rb.hasChanged()) {
            rb = lookupResourceBundleByEnumClassAndLocale(declaringClass);
            cache.put(declaringClassName, rb);
            resourceLoader.registerBundleAndFile(rb);
        }

        String keyAsStr = key.toString();
        String value = rb.getString(keyAsStr);
        if (args == null || args.length == 0) {
            return value;
        } else {
            return MessageFormat.format(value, args);
        }
    }

    private <E extends Enum<?>> CAL10NBundleExt lookupResourceBundleByEnumClassAndLocale(
            Class<E> declaringClass) throws MessageConveyorException {

        AnnotationExtractorViaEnumClass annotationExtractor = new AnnotationExtractorViaEnumClass(
                declaringClass);
        // basename is declared via an annotation on the declaringClass
        String baseName = annotationExtractor.getBaseName();
        if (baseName == null) {
            throw new MessageConveyorException(
                    "Missing @BaseName annotation in enum type ["
                            + declaringClass.getName() + "]. See also "
                            + CAL10NConstants.MISSING_BN_ANNOTATION_URL);
        }

        String charset = annotationExtractor.extractCharset(locale);
        if (charset.equals("")) {
            charset = "UTF-8";
        }
        // use the declaring class' loader instead of
        // this.getClass().getClassLoader()
        // see also http://jira.qos.ch/browse/CAL-8
        CAL10NBundleFinderByClassloaderExt cal10NBundleFinderByClassloader = new CAL10NBundleFinderByClassloaderExt(
                declaringClass.getClassLoader());
        CAL10NBundleExt rb = cal10NBundleFinderByClassloader.getBundle(baseName,
                locale, charset);

        if (rb == null) {
            throw new MessageConveyorException(
                    "Failed to locate resource bundle [" + baseName
                            + "] for locale [" + locale + "] for enum type ["
                            + declaringClass.getName() + "]");
        }
        return rb;
    }

    public String getMessage(MessageParameterObj mpo)
            throws MessageConveyorException {
        if (mpo == null) {
            throw new IllegalArgumentException(
                    "MessageParameterObj argumument cannot be null");
        }
        return getMessage(mpo.getKey(), mpo.getArgs());
    }
}
