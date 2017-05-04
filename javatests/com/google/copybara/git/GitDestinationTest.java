import static com.google.common.collect.Iterables.getOnlyElement;
import static com.google.copybara.ChangeMessage.parseMessage;
import static com.google.copybara.git.GitTestUtil.getGitEnv;
import com.google.common.truth.Truth;
import com.google.copybara.ChangeMessage;
import com.google.copybara.git.GitRepository.GitLogEntry;
import java.time.ZoneId;
import java.time.ZonedDateTime;
    return new GitRepository(path, /*workTree=*/null, /*verbose=*/true, getGitEnv());
    assertThat(repo().log(ref).run()).hasSize(expected);
    assertThat(parseMessage(lastCommit(branch).getBody())
        .labelsAsMultimap()).containsEntry(DummyOrigin.LABEL_NAME, originRef);
    assertThat(lastCommit(branch).getAuthor()).isEqualTo(author);
  }

  private GitLogEntry lastCommit(String ref) throws RepoException {
    return getOnlyElement(repo().log(ref).withLimit(1).run());
        firstCommitWriter(),
        firstCommitWriter(),
        firstCommitWriter(),
        newWriter(),
        firstCommitWriter(),
    process(firstCommitWriter(), ref);
    process(newWriter(), ref);
        newWriter(),
        newWriter(),
        firstCommitWriter(),
        newWriter(),
        newWriter(),
    Writer writer1 = destinationFirstCommit().newWriter(firstGlob, /*dryRun=*/false);
    Writer writer2 = destination().newWriter(Glob.createGlob(ImmutableList.of("baz/**")),
        /*dryRun=*/false);
    assertThat(destination().newWriter(firstGlob, /*dryRun=*/false)
        firstCommitWriter();
    writer = newWriter();
    writer = newWriter();
        firstCommitWriter(),
        newWriter()
    Truth.assertThat(checkPreviousImportReferenceMultipleParents()).isEqualTo("b2-origin");
  }
  @Test
  public void previousImportReferenceIsBeforeACommitWithMultipleParents_first_parent()
      throws Exception {
    options.gitDestination.lastRevFirstParent = true;
    Truth.assertThat(checkPreviousImportReferenceMultipleParents()).isEqualTo("b1-origin");
  }

  private String checkPreviousImportReferenceMultipleParents()
      throws IOException, RepoException, ValidationException {
    fetch = "b1";
    push = "b1";
    Files.write(scratchTree.resolve("master" + ".file"), ("master\n\n"
        + DummyOrigin.LABEL_NAME + ": should_not_happen").getBytes(UTF_8));
    scratchRepo.add().files("master" + ".file").run();
    scratchRepo.simpleCommand("commit", "-m", "master\n\n"
        + DummyOrigin.LABEL_NAME + ": should_not_happen");
    scratchRepo.simpleCommand("branch", "b1");
    scratchRepo.simpleCommand("branch", "b2");
    branchChange(scratchTree, scratchRepo, "b2", "b2-1\n\n"
        + DummyOrigin.LABEL_NAME + ": b2-origin");
    branchChange(scratchTree, scratchRepo, "b1", "b1-1\n\n"
        + DummyOrigin.LABEL_NAME + ": b1-origin");
    branchChange(scratchTree, scratchRepo, "b1", "b1-2");
    branchChange(scratchTree, scratchRepo, "b1", "b2-2");
    scratchRepo.simpleCommand("checkout", "b1");
    scratchRepo.simpleCommand("merge", "b2");
    return newWriter()
  private void branchChange(Path scratchTree, GitRepository scratchRepo, final String branch,
      String msg) throws RepoException, IOException {
    scratchRepo.simpleCommand("checkout", branch);
    Files.write(scratchTree.resolve(branch + ".file"), msg.getBytes(UTF_8));
    scratchRepo.add().files(branch + ".file").run();
    scratchRepo.simpleCommand("commit", "-m", msg);
  }

        firstCommitWriter(),
        new DummyRevision("first_commit").withTimestamp(timeFromEpoch(1414141414)));
    GitTesting.assertAuthorTimestamp(repo(), "master", timeFromEpoch(1414141414));
        newWriter(),
        new DummyRevision("second_commit").withTimestamp(timeFromEpoch(1515151515)));
    GitTesting.assertAuthorTimestamp(repo(), "master", timeFromEpoch(1515151515));
  }

  static ZonedDateTime timeFromEpoch(long time) {
    return ZonedDateTime.ofInstant(Instant.ofEpochSecond(time), ZoneId.of("-07:00"));
        firstCommitWriter(),
        firstCommitWriter(),
        new DummyRevision("first_commit").withTimestamp(timeFromEpoch(1414141414)));
        newWriter(),
        new DummyRevision("second_commit").withTimestamp(timeFromEpoch(1414141490)));
        firstCommitWriter(),
        new DummyRevision("first_commit").withTimestamp(timeFromEpoch(1414141414)));
        newWriter(),
        new DummyRevision("second_commit").withTimestamp(timeFromEpoch(1414141490)));
        firstCommitWriter(),
        firstCommitWriter(),
        .withTimestamp(timeFromEpoch(1414141414));
        firstCommitWriter(),
        newWriter(),
        newWriter(),
    process(firstCommitWriter(), ref);
    process(newWriter(), ref);
        newWriter(), ref, firstCommit);
    process(firstCommitWriter(), ref);
    process(newWriter(), ref);
        newWriter(), ref, firstCommit);
    process(firstCommitWriter(), ref);
    process(newWriter(), ref);
        newWriter(), ref, firstCommit);
        firstCommitWriter();
    process(firstCommitWriter(), ref);
    process(newWriter(), ref);
    Writer writer = newWriter();
        getGitEnv());
    ImmutableList<GitLogEntry> entries = localRepo.log("HEAD").run();
    assertThat(entries.get(0).getBody()).isEqualTo(""
        + "test summary\n"
        + "DummyOrigin-RevId: origin_ref2\n");

    assertThat(entries.get(1).getBody()).isEqualTo(""
        + "DummyOrigin-RevId: origin_ref1\n");

    assertThat(entries.get(2).getBody()).isEqualTo("change\n");

    Writer writer = firstCommitWriter();
    String body = lastCommit("HEAD").getBody();
    assertThat(body).isEqualTo("This is a message\n"
        + "DummyOrigin-RevId: first_commit\n");
    // Double check that we can access it as a label.
    assertThat(ChangeMessage.parseMessage(body).labelsAsMultimap())
        .containsEntry("DummyOrigin-RevId", "first_commit");
        firstCommitWriter(), ref1);
    process(newWriter(), ref2);

  private Writer newWriter() throws ValidationException {
    return destination().newWriter(destinationFiles, /*dryRun=*/ false);
  }

  private Writer firstCommitWriter() throws ValidationException {
    return destinationFirstCommit().newWriter(destinationFiles, /*dryRun=*/ false);
  }