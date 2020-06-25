# Filters

The CredentialsProvider implementation in this plugin calls `secretsmanager:ListSecrets` to cache the secrets' metadata. At this time, Secrets Manager does not support server-side restrictions on this list, so it returns all secrets in the AWS account, whether you have given Jenkins the `secretsmanager:GetSecretValue` permission to actually use those secrets or not. This can result in unwanted entries appearing in the credentials UI, which users will mistake for usable credentials. 

To improve the user experience of this aspect of Jenkins, you can specify optional filters in the plugin configuration. Only the secrets that match the filter criteria will be presented through the CredentialsProvider. This hides unwanted entries from the credentials UI. 

Notes:

- These are client-side filters. As such they only provide usability benefits. They have no security benefits, as Jenkins still fetches the full secret list from AWS.
- The `SecretSource` implementation does not use the filters, as they are not relevant to it.

## Tags

You can choose to only show credentials that have tags with particular keys (or particular keys and values).

Multiple tag filters combine with an **implicit OR** operator: a credential will be available if it matches **any** of the tag filter criteria.

Example: show credentials with the tag `foo` = [any value]:

```yaml
unclassified:
  awsCredentialsProvider:
    filters:
      tags:
        - key: foo
```

Example: show credentials with the tag `foo` = `bar`:

```yaml
unclassified:
  awsCredentialsProvider:
    filters:
      tags:
        - key: foo
          value: bar
```
 
Example: show credentials with the tags `foo` = `bar` OR `baz` = `qux`.

```yaml
unclassified:
  awsCredentialsProvider:
    filters:
      tags:
        - key: foo
          value: bar
        - key: baz
          value: qux
```