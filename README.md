* Fork from https://github.com/sparsick/comparison-java-ssh-libs.git
* Change to ftp Client, and Add Apache Commons Net Lib.
# Performance Test
* Download 1MB File
1. Apache Commons Net average 163 ms
2. Apache Commons VFS2 179 ms

* Download 15MB File
1. Apache Commons Net average 1577 ms
2. Apache Commons VFS2 1615 ms

# Other
1. Apache Commons Net is the best ftp download performance, but tiny difference
2. Apache Commons Net last release version 3.8.0 at Feb 19, 2021, that have some vulnerabilities from old Apache Commons IO.
4. Apache Commons VFS2 last release version 2.9.0 at Jul 20, 2021, that have some vulnerabilities from old Apache Log4j2.

# Reference
* Apache Commons Net https://commons.apache.org/proper/commons-net/
* Apache Commons VFS2 https://commons.apache.org/proper/commons-vfs/