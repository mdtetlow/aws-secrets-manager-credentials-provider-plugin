## Cross-Account Access

The plugin can access secrets stored in other AWS accounts.

## Setup

For each secondary AWS account:

1. Create the [cross-account role](http://docs.aws.amazon.com/IAM/latest/UserGuide/tutorial_cross-account-with-roles.html) and associated policies in AWS.
2. Test that Jenkins can assume the role and retrieve secrets from the other account.
3. Add the role ARN to the `roles` list in the plugin configuration.

```yaml
unclassified:
  awsCredentialsProvider:
    beta:
      roles:
        - arn:aws:iam::111111111111:role/foo
        - arn:aws:iam::222222222222:role/bar
```

## Considerations

**Do not add more AWS accounts than necessary.** Each additional AWS account necessitates another set of HTTP requests to retrieve secrets. This increases the time to populate the credential list. It also increases the risk of service degradation, as any of those requests could fail.

## Limitations

- The secret name must be unique across all AWS accounts that Jenkins uses. The credential provider will stop working when duplicate secret names are present: if this happens, delete or rename the duplicates, and then retry the failed Jenkins operation.
- Access to Secrets Manager in the primary account cannot currently be turned off in the plugin. This might be a problem if you only use the primary account as an entry point to other accounts, and don't intend Jenkins to access data in the primary account.