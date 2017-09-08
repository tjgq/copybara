import com.google.copybara.git.GitCredential.UserPassword;
import javax.annotation.Nullable;
  private boolean force;
    force = false;
    return GitRepository.newBareRepo(path, getGitEnv(),  /*verbose=*/true);
    options.setForce(force);
  private void process(Writer<GitRevision> writer, DummyRevision originRef)
  private void processWithBaseline(Writer<GitRevision> writer, DummyRevision originRef,
  private void processWithBaselineAndConfirmation(Writer<GitRevision> writer,
      DummyRevision originRef, String baseline, boolean askForConfirmation)
        .matchesNext(MessageType.PROGRESS, "Git Destination: Fetching: file:.* master")
        .matchesNext(MessageType.WARNING, "Git Destination: 'master' doesn't exist in 'file://.*")
    thrown.expect(ValidationException.class);
    Writer<GitRevision> writer1 = firstCommitWriter();
    Writer<GitRevision> writer2 = destination().newWriter(
        Glob.createGlob(ImmutableList.of("baz/**")),/*dryRun=*/false, /*groupId=*/null, writer1);
    assertThat(destination().newWriter(firstGlob, /*dryRun=*/false, /*groupId=*/null, writer2)
        .getDestinationStatus(ref1.getLabelName()).getBaseline())
    assertThat(writer2.getDestinationStatus(ref2.getLabelName()).getBaseline())
    checkPreviousImportReference();
  }

  @Test
  public void test_force_rewrite_history() throws Exception {
    fetch = "master";
    push = "feature";

    destinationFiles = Glob.createGlob(ImmutableList.of("**"), ImmutableList.of("excluded.txt"));

    Path scratchTree = Files.createTempDirectory("GitDestinationTest-scratchTree");
    Files.write(scratchTree.resolve("excluded.txt"), "some content".getBytes(UTF_8));
    repo().withWorkTree(scratchTree).add().files("excluded.txt").run();
    repo().withWorkTree(scratchTree).simpleCommand("commit", "-m", "master change");

    Path file = workdir.resolve("test.txt");

    Files.write(file, "some content".getBytes());
    Writer<GitRevision> writer = newWriter();

    assertThat(writer.getDestinationStatus(DummyOrigin.LABEL_NAME)).isNull();
    process(writer, new DummyRevision("first_commit"));
    assertCommitHasOrigin("feature", "first_commit");

    Files.write(file, "changed".getBytes());

    process(writer, new DummyRevision("second_commit"));
    assertCommitHasOrigin("feature", "second_commit");

    GitRevision oldHead = repo().resolveReference("HEAD", /*contextRef=*/null);

    options.gitDestination.nonFastForwardPush = true;

    Files.write(file, "some content".getBytes());
    writer = newWriter();

    assertThat(writer.getDestinationStatus(DummyOrigin.LABEL_NAME)).isNull();
    process(writer, new DummyRevision("first_commit_2"));
    assertCommitHasOrigin("feature", "first_commit_2");

    Files.write(file, "changed".getBytes());

    process(writer, new DummyRevision("second_commit_2"));
    assertCommitHasOrigin("feature", "second_commit_2");

    assertThat(repo().log("master..feature").run()).hasSize(2);
  }

  @Test
  public void previousImportReference_with_force() throws Exception {
    force = true;
    checkPreviousImportReference();
  }

  private void checkPreviousImportReference()
      throws IOException, ValidationException, RepoException {
    Writer<GitRevision>writer =
    assertThat(writer.getDestinationStatus(DummyOrigin.LABEL_NAME)).isNull();
    assertThat(writer.getDestinationStatus(DummyOrigin.LABEL_NAME).getBaseline())
    assertThat(writer.getDestinationStatus(DummyOrigin.LABEL_NAME).getBaseline())
    assertThat(newWriter().getDestinationStatus(DummyOrigin.LABEL_NAME).getBaseline())
    return newWriter().getDestinationStatus(DummyOrigin.LABEL_NAME).getBaseline();
    processWithBaseline(newWriter(), ref, firstCommit);
    Writer<GitRevision> writer = firstCommitWriter();
    localRepo.push().run();
    Writer<GitRevision> writer = destination().newWriter(destinationFiles, /*dryRun=*/ true,
                                            /*groupId=*/null, /*oldWriter=*/null);
    writer = newWriter(writer);
    localRepo.push().run();
    Writer<GitRevision> writer = newWriter();
    GitRepository localRepo = GitRepository.newRepo(true, localPath, getGitEnv()).init(
    );
    Writer<GitRevision> writer = firstCommitWriter();
    newWriter().visitChanges(null,
          return input.getLabels().get(DummyOrigin.LABEL_NAME).contains("origin_ref1")
    assertThat(visited.get(0).getLabels().get(DummyOrigin.LABEL_NAME)).containsExactly(
        "origin_ref2");
    assertThat(visited.get(1).getLabels().get(DummyOrigin.LABEL_NAME)).containsExactly(
        "origin_ref1");
  }

  @Test
  public void testCredentials() throws Exception {
    checkCredentials();
  }

  @Test
  public void testCredentials_localRepo() throws Exception {
    Path path = Files.createTempDirectory("local");
    options.gitDestination.localRepoPath = path.toString();
    GitRepository repository = checkCredentials();
    assertThat(repository.getGitDir().toString()).isEqualTo(path.resolve(".git").toString());
  private GitRepository checkCredentials() throws IOException, RepoException, ValidationException {
    Path credentialsFile = Files.createTempFile("credentials", "test");
    Files.write(credentialsFile, "https://user:SECRET@somehost.com".getBytes(UTF_8));
    options.git.credentialHelperStorePath = credentialsFile.toString();

    GitRepository repository = destinationFirstCommit().getLocalRepo().get(console);
    UserPassword result = repository
        .credentialFill("https://somehost.com/foo/bar");

    assertThat(result.getUsername()).isEqualTo("user");
    assertThat(result.getPassword_BeCareful()).isEqualTo("SECRET");
    return repository;
  }

  private Writer<GitRevision> newWriter() throws ValidationException {
    return newWriter(/*oldWriter=*/null);
  }

  private Writer<GitRevision> newWriter(@Nullable Writer<GitRevision> oldWriter)
      throws ValidationException {
    return destination().newWriter(destinationFiles, /*dryRun=*/ false, /*groupId=*/null,
        oldWriter);
  private Writer<GitRevision> firstCommitWriter() throws ValidationException {
    return destinationFirstCommit().newWriter(destinationFiles, /*dryRun=*/ false,
                                              /*groupId=*/null, /*oldWriter=*/null);
  }

  @Test
  public void testMapReferences() throws Exception {
    Files.write(workdir.resolve("test.txt"), "one".getBytes());
    Writer<GitRevision> writer = firstCommitWriter();
    process(writer, new DummyRevision("1"));

    Files.write(workdir.resolve("test.txt"), "two".getBytes());
    writer = newWriter(writer);
    process(writer, new DummyRevision("2"));

    Files.write(workdir.resolve("test.txt"), "three".getBytes());
    process(writer, new DummyRevision("3"));

    writer.visitChanges(/*start=*/ null, ignore -> VisitResult.CONTINUE);

    Files.write(workdir.resolve("test.txt"), "four".getBytes());
    process(writer, new DummyRevision("4"));