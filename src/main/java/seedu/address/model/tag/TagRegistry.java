package seedu.address.model.tag;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import seedu.address.model.listing.Listing;
import seedu.address.model.person.PropertyPreference;
import seedu.address.model.tag.exceptions.DuplicateTagException;
import seedu.address.model.tag.exceptions.TagNotFoundException;

/**
 * Represents a hashmap of tags that enforces uniqueness between its elements and does not allow nulls.
 * The key of the hashmap is the tag name in uppercase to ensure case insensitivity. The tag name acts as a unique
 * identifier for the tags, as such only one tag should exist for a particular tag name. Since a hashmap is used,
 * adding, updating  of tags are ensured to remain unique. Deleting of tags also ensures that the
 * intended tag is deleted.
 *
 * Supports a minimal set of hashmap operations.
 *
 * TagRegistry is a singleton.
 */
public class TagRegistry implements Iterable<Tag> {

    // Singleton instance
    private static final TagRegistry SINGLETON = new TagRegistry();

    private final ObservableMap<String, Tag> internalHashmap = FXCollections.observableHashMap();
    private final ObservableMap<String, Tag> internalUnmodifiableMap =
            FXCollections.unmodifiableObservableMap(internalHashmap);

    // Private constructor to prevent instantiation
    private TagRegistry() {}

    /**
     * Returns the singleton instance of the TagRegistry.
     * Factory method of the TagRegistry class.
     *
     * @return singleton instance of the TagRegistry.
     */
    public static TagRegistry of() {
        return SINGLETON;
    }

    /**
     * Checks if the hashmap contains an equivalent tag with the same tag name as the given argument.
     *
     * @param toCheck the tag to check if it exists in the hashmap as a key.
     * @return true if the list contains an equivalent tag with the same tag name as the given argument.
     *         false otherwise.
     */
    public boolean contains(Tag toCheck) {
        requireNonNull(toCheck);
        return internalHashmap.containsKey(toCheck.getTagName());
    }

    /**
     * Gets the existing tag with the specified tag name.
     * {@code tagName} must exist in the hashmap.
     *
     * @param tagName the name of the tag to get.
     * @return tag with specified tag name.
     * @throws TagNotFoundException if tag is not found.
     */
    public Tag get(String tagName) {
        requireNonNull(tagName);

        String upperTagName = tagName.toUpperCase();
        if (!internalHashmap.containsKey(upperTagName)) {
            throw new TagNotFoundException();
        }

        return internalHashmap.get(upperTagName);
    }

    /**
     * Adds a tag with the given tag name to the hashmap.
     * {@code tag} must not exist in the hashmap.
     *
     * @param toAdd the tag to add.
     * @throws DuplicateTagException if the tag name to add already exists in the hashmap.
     */
    public void add(Tag toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateTagException();
        }
        internalHashmap.put(toAdd.getTagName(), toAdd);
    }

    /**
     * Replaces the tag {@code target} in the hashmap with {@code editedTag}.
     * {@code target} must exist in the hashmap.
     * The new key of {@code editedTag} must not be the same as another existing tag in the hashmap.
     *
     * @param target the tag to be replaced.
     * @param editedTag the tag to replace the target tag with.
     * @throws DuplicateTagException if the replacement is equivalent to another existing tag in the hashmap.
     */
    public void setTag(Tag target, Tag editedTag) {
        requireAllNonNull(target, editedTag);

        if (!contains(target)) {
            throw new TagNotFoundException();
        }

        if (!target.isSameTag(editedTag) && contains(editedTag)) {
            throw new DuplicateTagException();
        }

        remove(target);
        add(editedTag);
    }


    /**
     * Removes the tag with the specified tag name from the hashmap.
     * {@code tagName} must exist in the hashmap.
     *
     * @param toRemove the tag to remove.
     * @throws TagNotFoundException if tag is not found.
     */
    public void remove(Tag toRemove) {
        requireNonNull(toRemove);

        if (!contains(toRemove)) {
            throw new TagNotFoundException();
        }

        internalHashmap.remove(toRemove.getTagName());
    }

    /**
     * Replaces the contents of this hashmap with {@code tags}.
     * {@code tags} must not contain duplicate tags.
     *
     * @param tags the replacement list.
     */
    public void setTags(List<Tag> tags) {
        requireAllNonNull(tags);

        if (!tagsAreUnique(tags)) {
            throw new DuplicateTagException();
        }

        internalHashmap.clear();
        for (Tag tag : tags) {
            internalHashmap.put(tag.getTagName(), tag);
        }
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableMap}.
     *
     * @return the unmodifiable map.
     */
    public ObservableMap<String, Tag> asUnmodifiableObservableMap() {
        return internalUnmodifiableMap;
    }

    @Override
    public Iterator<Tag> iterator() {
        return internalHashmap.values().iterator();
    }

    @Override
    public int hashCode() {
        return internalHashmap.hashCode();
    }

    @Override
    public String toString() {
        return internalHashmap.toString();
    }

    /**
     * Checks if {@code tags} contains only unique tags.
     *
     * @return true if {@code tags} contains only unique tags. false otherwise.
     */
    private boolean tagsAreUnique(List<Tag> tags) {
        Set<String> seenNames = new HashSet<>();
        for (Tag tag : tags) {
            if (!seenNames.add(tag.getTagName())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Adds the association of {@code newPropertyPreference} into the tag with the name {@code targetTagName} in the
     * hashmap.
     * {@code targetTagName} must exist in the hashmap.
     *
     * @param targetTagName the tag name of the tag.
     * @param newPropertyPreference the property preference to add to the tag.
     * @return the tag that has been modified.
     * @throws TagNotFoundException if the tag name does not exist in the hashmap.
     */
    public Tag addPropertyPreferenceToTag(String targetTagName, PropertyPreference newPropertyPreference) {

        requireAllNonNull(targetTagName, newPropertyPreference);
        checkArgument(Tag.isValidTagName(targetTagName), Tag.MESSAGE_CONSTRAINTS);

        String upperTagName = targetTagName.toUpperCase();
        Tag tag = internalHashmap.get(upperTagName);
        if (tag == null && !contains(tag)) {
            throw new TagNotFoundException();
        }

        List<PropertyPreference> updatedPropertyPreferences = new ArrayList<>(tag.getPropertyPreferences());
        updatedPropertyPreferences.add(newPropertyPreference);

        Tag editedTag = new Tag(tag.tagName, updatedPropertyPreferences, tag.getListings());
        internalHashmap.put(upperTagName, editedTag);

        return editedTag;
    }

    /**
     * Adds the association of {@code newListing} into the tag with the name {@code targetTagName} in the
     * hashmap.
     * {@code targetTagName} must exist in the hashmap.
     *
     * @param targetTagName the tag name of the tag.
     * @param newListing the listing to add to the tag.
     * @return the tag that has been modified.1
     * @throws TagNotFoundException if the tag name does not exist in the hashmap.
     */
    public Tag addListingToTag(String targetTagName, Listing newListing) {

        requireAllNonNull(targetTagName, newListing);
        checkArgument(Tag.isValidTagName(targetTagName), Tag.MESSAGE_CONSTRAINTS);

        String upperTagName = targetTagName.toUpperCase();
        Tag tag = internalHashmap.get(upperTagName);
        if (tag == null && !contains(tag)) {
            throw new TagNotFoundException();
        }

        List<Listing> updatedListings = new ArrayList<>(tag.getListings());
        updatedListings.add(newListing);

        Tag editedTag = new Tag(tag.tagName, tag.getPropertyPreferences(), updatedListings);
        internalHashmap.put(upperTagName, editedTag);

        return editedTag;
    }
}
